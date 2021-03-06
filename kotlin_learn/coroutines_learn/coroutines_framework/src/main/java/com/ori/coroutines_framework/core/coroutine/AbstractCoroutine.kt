package com.example.coroutines_base_learn.core.coroutine

import com.example.coroutines_base_learn.CancellationException
import com.example.coroutines_base_learn.Job
import com.example.coroutines_base_learn.OnCancel
import com.example.coroutines_base_learn.OnComplete
import com.example.coroutines_base_learn.core.cancel.suspendCancellableCoroutine
import com.example.coroutines_base_learn.core.scope.CoroutineScope
import com.example.coroutines_base_learn.core.status.CancellationHandlerDisposable
import com.example.coroutines_base_learn.core.status.CompletionHandlerDisposable
import com.example.coroutines_base_learn.core.status.CoroutineState
import com.example.coroutines_base_learn.core.status.Disposable
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

abstract class AbstractCoroutine<T>(context: CoroutineContext) : Job, Continuation<T>,
    CoroutineScope {
    val state = AtomicReference<CoroutineState>()
    override val context: CoroutineContext

    protected val parentJob = context[Job]

    private var parentCancelDisposable: Disposable? = null

    override val scopeContext: CoroutineContext
        get() = context

    init {
        /**初始状态都是InComplete*/
        state.set(CoroutineState.InComplete())
        this.context = context + this
        parentCancelDisposable = parentJob?.invokeOnCancel {
            cancel()
        }
    }

    /**
     * 根据状态类型判断是否完成此job
     * */
    val isCompleted
        get() = state.get() is CoroutineState.Complete<*>

    /**
     * 出于InComplete状态，是活跃状态
     * */
    override val isActive: Boolean
        get() = when (state.get()) {
            is CoroutineState.Complete<*>, is CoroutineState.Cancelling -> false
            else -> true
        }


    protected open fun handleJobException(e: Throwable) = false

    protected open fun handleChildException(e: Throwable): Boolean {
        cancel()
        return tryHandleException(e)
    }

    private fun tryHandleException(e: Throwable): Boolean {
        return when (e) {
            is CancellationException -> {
                false
            }
            else -> {
                (parentJob as? AbstractCoroutine<*>)?.handleChildException(e)?.takeIf { it }
                    ?: handleJobException(e)
            }
        }
    }




    override suspend fun join() {
        when (state.get()) {
            is CoroutineState.InComplete, is CoroutineState.Cancelling -> return joinSuspend()
            is CoroutineState.Complete<*> -> {
                val currentCallingJobState = coroutineContext[Job]?.isActive ?: return
                if (!currentCallingJobState) {
                    throw CancellationException("Coroutine is cancelled")
                }
                return
            }
        }
    }

    private suspend fun joinSuspend() = suspendCancellableCoroutine<Unit> { continuation ->
        val disposable = doOnCompleted { result -> continuation.resume(Unit) }
        continuation.invokeOnCancellation { disposable.dispose() }
    }

    /*suspendCoroutine<Unit> { continuation ->
    doOnCompleted { continuation.resume(Unit) }
}*/

    override fun cancel() {
        val newState = state.getAndUpdate {
            when (it) {
                /**
                 * 将协程的状态从InComplete转为Cancelling
                 * 将回调队列复制到Cancelling中
                 * */
                is CoroutineState.InComplete -> {
                    CoroutineState.Cancelling().from(it)
                }
                is CoroutineState.Cancelling, is CoroutineState.Complete<*> -> it
            }
        }
        /**
         * 这里的newState返回的旧的状态，
         * */
        takeIf { newState is CoroutineState.InComplete }.let {
            /**移除旧状态下的所有回调*/
            newState.notifyCancellation()
            /**清空回调列表*/
            newState.clear()
        }

    }

    /**
     *
     * */
    override fun invokeOnCancel(onCancel: OnCancel): Disposable {
        val disposable = CancellationHandlerDisposable(this, onCancel)
        val newState = state.updateAndGet {
            when (it) {
                is CoroutineState.InComplete -> {
                    /**复制状态，注册新的回调*/
                    CoroutineState.InComplete().from(it).with(disposable)
                }
                is CoroutineState.Cancelling, is CoroutineState.Complete<*> -> {
                    it
                }
            }
        }
        (newState as? CoroutineState.Cancelling)?.let {
            onCancel()
        }
        return disposable
    }

    fun doOnCompleted(block: (Result<T>) -> Unit): Disposable {
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
                is CoroutineState.Cancelling, is CoroutineState.InComplete -> {
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