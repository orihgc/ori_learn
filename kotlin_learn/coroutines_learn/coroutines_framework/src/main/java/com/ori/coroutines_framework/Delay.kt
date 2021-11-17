package com.example.coroutines_base_learn

import com.example.coroutines_base_learn.core.cancel.suspendCancellableCoroutine
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
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

