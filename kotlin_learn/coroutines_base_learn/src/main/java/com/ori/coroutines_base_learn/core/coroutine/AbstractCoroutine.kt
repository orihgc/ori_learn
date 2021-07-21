package com.ori.coroutines_base_learn.core.coroutine

import com.ori.coroutines_base_learn.Job
import com.ori.coroutines_base_learn.OnCancel
import com.ori.coroutines_base_learn.OnComplete
import com.ori.coroutines_base_learn.core.status.CompletionHandlerDisposable
import com.ori.coroutines_base_learn.core.status.CoroutineState
import com.ori.coroutines_base_learn.core.status.Disposable
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class AbstractCoroutine<T>(context: CoroutineContext) : Job, Continuation<T> {
    private val state = AtomicReference<CoroutineState>()
    override val context: CoroutineContext

    init {
        /**初始状态都是InComplete*/
        state.set(CoroutineState.InComplete())
        this.context = context + this
    }

    /**
     * 根据状态类型判断是否完成此job
     * */
    val isCompleted
        get() = state.get() is CoroutineState.Complete<*>

    /**
     *
     * */
    override val isActive: Boolean
        get() = when (state.get()) {
            is CoroutineState.Complete<*>,
            is CoroutineState.Cancelling -> false
            else -> true
        }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override suspend fun join() {
        when (state.get()) {
            is CoroutineState.InComplete,
            is CoroutineState.Cancelling -> return joinSuspend()
            is CoroutineState.Complete<*> -> return
        }
    }

    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        doOnCompleted { continuation.resume(Unit) }
    }

    override fun invokeOnCancel(onCancel: OnCancel): Disposable {
        TODO("Not yet implemented")
    }

    private fun doOnCompleted(block: (Result<T>) -> Unit): Disposable {
        val disposable = CompletionHandlerDisposable(this, block)
        val newState = state.updateAndGet {
            when (it) {
                is CoroutineState.InComplete -> {
                    CoroutineState.InComplete().from(it).with(disposable)
                }
                is CoroutineState.Cancelling -> {
                    CoroutineState.Cancelling().from(it).with(disposable)
                }
                is CoroutineState.Complete<*> -> {
                    it
                }
            }
        }
        (newState as? CoroutineState.Complete<T>)?.let {
            block(
                when {
                    it.exception != null -> Result.failure(it.exception)
                    it.value != null -> Result.success(it.value)
                    else -> throw IllegalStateException("Won't happen")
                }
            )
        }
        return disposable
    }

    override fun invokeOnCompletion(onComplete: OnComplete): Disposable {
        return doOnCompleted { onComplete() }
    }

    override fun remove(disposable: Disposable) {
        state.updateAndGet {
            when (it) {
                is CoroutineState.InComplete -> {
                    CoroutineState.InComplete().from(it).withOut(disposable)

                }
                is CoroutineState.Cancelling -> {
                    CoroutineState.Cancelling().from(it).withOut(disposable)
                }
                is CoroutineState.Complete<*> -> {
                    it
                }
            }
        }
    }

    override fun attachChild(child: Job): Disposable {
        TODO("Not yet implemented")
    }

    override fun resumeWith(result: Result<T>) {
        val newState = state.updateAndGet {
            when (it) {
                //although cancelled, flows of job may work out with the normal result.
                is CoroutineState.Cancelling,
                is CoroutineState.InComplete -> {
                    CoroutineState.Complete(result.getOrNull(), result.exceptionOrNull()).from(it)
                }
                is CoroutineState.Complete<*> -> {
                    throw IllegalStateException("Already completed!")
                }
            }
        }
        newState.notifyCompletion(result)
        newState.clear()
    }
}