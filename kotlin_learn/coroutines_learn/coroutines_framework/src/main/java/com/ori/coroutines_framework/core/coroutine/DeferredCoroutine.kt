package com.ori.coroutines_base_learn.core.coroutine

import com.ori.coroutines_base_learn.Job
import com.ori.coroutines_base_learn.Deferred
import com.ori.coroutines_base_learn.core.status.CoroutineState
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine

class DeferredCoroutine<T>(context: CoroutineContext) : AbstractCoroutine<T>(context), Deferred<T> {

    override suspend fun await(): T {
        return when (val currentState = state.get()) {
            is CoroutineState.InComplete,
            is CoroutineState.Cancelling -> awaitSuspend()
            is CoroutineState.Complete<*> -> {
                coroutineContext[Job] ?.isActive ?.takeIf { !it }?.let {
                    throw CancellationException("Coroutine is cancelled.")
                }
                currentState.exception?.let { throw it } ?: (currentState.value as T)
            }
        }
    }

    private suspend fun awaitSuspend() = suspendCoroutine<T> { continuation ->
        val disposable = doOnCompleted { result ->
            continuation.resumeWith(result)
        }
    }
}