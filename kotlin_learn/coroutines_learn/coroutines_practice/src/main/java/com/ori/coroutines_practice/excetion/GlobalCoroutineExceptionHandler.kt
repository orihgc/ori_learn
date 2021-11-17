package com.ori.coroutines_practice.excetion

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Global Coroutine exception:$exception")
    }
}

