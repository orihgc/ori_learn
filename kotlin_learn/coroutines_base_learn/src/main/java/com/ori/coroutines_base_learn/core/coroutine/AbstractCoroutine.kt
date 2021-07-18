package com.ori.coroutines_base_learn.core.coroutine

import com.ori.coroutines_base_learn.Job
import com.ori.coroutines_base_learn.OnCancel
import com.ori.coroutines_base_learn.OnComplete
import com.ori.coroutines_base_learn.core.status.CoroutineState
import com.ori.coroutines_base_learn.core.status.Disposable
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

abstract class AbstractCoroutine<T>(context: CoroutineContext) : Job, Continuation<T> {
    protected val state = AtomicReference<CoroutineState>()
    override val context: CoroutineContext

    init {
        state.set(CoroutineState.InComplete())
        this.context = context + this
    }

    val isCompleted
        get() = state.get() is CoroutineState.Complete<*>

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
        TODO("Not yet implemented")
    }

    override fun invokeOnCancel(onCancel: OnCancel): Disposable {
        TODO("Not yet implemented")
    }

    override fun invokeOnCompletion(onComplete: OnComplete): Disposable {
        TODO("Not yet implemented")
    }

    override fun remove(disposable: Disposable) {
        TODO("Not yet implemented")
    }

    override fun attachChild(child: Job): Disposable {
        TODO("Not yet implemented")
    }

    override fun resumeWith(result: Result<T>) {
        TODO("Not yet implemented")
    }


}