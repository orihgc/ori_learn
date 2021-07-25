package com.ori.coroutines_base_learn

import com.ori.coroutines_base_learn.core.cancel.suspendCancellableCoroutine
import com.ori.coroutines_base_learn.core.dispatcher.Dispatchers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*


private val executor = Executors.newScheduledThreadPool(1) { runnable ->
    Thread(runnable, "Scheduler").apply { isDaemon = true }
}

suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
    if (time < 0) {
        return
    }

    suspendCancellableCoroutine<Unit> { continuation ->
        val future = executor.schedule({ continuation.resume(Unit) }, time, unit)
        continuation.invokeOnCancellation { future.cancel(true) }
    }

    /**/
    /*
    suspendCoroutine<Unit> { continuation ->
        executor.schedule({ continuation.resume(Unit) }, time, unit)
    }
    */
}

