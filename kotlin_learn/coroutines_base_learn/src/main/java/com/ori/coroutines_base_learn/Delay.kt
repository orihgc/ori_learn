package com.ori.coroutines_base_learn

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

    suspendCoroutine<Unit> { continuation ->
        executor.schedule({ continuation.resume(Unit) }, time, unit)
    }
}

fun main() {
    val suspend = suspend {
        delay(5000)
        5
    }.createCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("$result")
        }

    })
    thread {
        Thread.sleep(6000)
    }
    suspend.resume(Unit)
}