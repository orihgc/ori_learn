package com.ori.coroutines_practice.practice

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

suspend fun main() {
    safeTest5()
}

suspend fun safeTest1() {
    var count = 0
    List(100) {
        GlobalScope.launch { count++ }
    }.joinAll()
    print(count)
}

suspend fun safeTest2() {
    val count = AtomicInteger(0)
    List(100) {
        GlobalScope.launch { count.incrementAndGet() }
    }.joinAll()
    print(count)
}

suspend fun safeTest3() {
    var count = 0
    val mutex = Mutex()
    List(100) {
        GlobalScope.launch {
            mutex.withLock {
                count++
            }
        }
    }.joinAll()
    print(count)

}

suspend fun safeTest4() {
    var count = 0
    val semaphore = Semaphore(1)
    List(100) {
        GlobalScope.launch {
            semaphore.withPermit {
                count++
            }
        }
    }.joinAll()
    print(count)
}

suspend fun safeTest5() {
    val count = 0
    val result = count + List(1000) {
        GlobalScope.async { 1 }
    }.map {
        it.await()
    }.sum()
    print(result)
}
