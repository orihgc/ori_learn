package com.example.coroutines_base_learn.core.scope

import com.example.coroutines_base_learn.core.coroutine.AbstractCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

interface CoroutineScope {
    val scopeContext: CoroutineContext
}

suspend fun <R> coroutineScope(block: suspend CoroutineScope.() -> R): R =
    suspendCoroutine { continuation ->
        val coroutine = ScopeCoroutine(continuation.context, continuation)
        block.startCoroutine(coroutine, coroutine)
    }

internal open class ScopeCoroutine<T>(
    context: CoroutineContext,
    protected val continuation: Continuation<T>
) : AbstractCoroutine<T>(context) {

    override fun resumeWith(result: Result<T>) {
        super.resumeWith(result)
        continuation.resumeWith(result)
    }
}