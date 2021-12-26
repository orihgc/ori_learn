package com.example.coroutines

import kotlinx.coroutines.*

fun main() {
    cancelJob()
}

fun cancelJob() {
    runBlocking {
        val job = launch {
            repeat(1000) {
                println("job I'm sleep")
                delay(500)
            }
        }
        delay(3000)
        println("main:I'm tired waiting")
        job.cancel()
        job.join()
        /**相当于*/
        job.cancelAndJoin()
        println("main: Now I can quit")
    }
}

