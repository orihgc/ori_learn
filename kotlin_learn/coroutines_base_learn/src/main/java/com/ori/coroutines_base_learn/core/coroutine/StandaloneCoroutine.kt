package com.ori.coroutines_base_learn.core.coroutine

import com.ori.coroutines_base_learn.core.exception.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class StandaloneCoroutine(context: CoroutineContext):AbstractCoroutine<Unit>(context) {

    override fun handleJobException(e: Throwable): Boolean {
        super.handleJobException(e)
        context[CoroutineExceptionHandler]?.handleException(context, e) ?:
        Thread.currentThread().let { it.uncaughtExceptionHandler.uncaughtException(it, e) }
        return true
    }

}