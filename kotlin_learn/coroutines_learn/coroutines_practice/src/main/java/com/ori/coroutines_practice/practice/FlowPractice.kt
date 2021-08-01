package com.ori.coroutines_practice.practice

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.ArithmeticException

suspend fun main() {
    testFlow2()

}

val intFlow = flow {
    emit(1)
    throw ArithmeticException("")
}.catch {
    print("caught error: $it")
}.onCompletion {
    println("finally")
}


suspend fun testFlow1() {
    GlobalScope.launch(Dispatchers.Unconfined) {
        intFlow.flowOn(Dispatchers.IO).collect {
            println(it)
            val coroutineName = coroutineContext[CoroutineName]
            println(coroutineName)
        }
    }.join()
}

suspend fun testFlow2() {
    GlobalScope.launch {
        intFlow.collect { println(it) }
        intFlow.collect { println(it) }
    }.join()
}

fun createFlow() = flow<Int> {
    (1..3).forEach {
        emit(it)
        delay(100)
    }
}.onEach {
    println(it)
}

suspend fun testFlow3() {
    GlobalScope.launch {
        createFlow().collect()
    }

    createFlow().launchIn(GlobalScope)
}

suspend fun testFlow4() {
    val job = GlobalScope.launch {
        val flow = flow<Int> {
            (1..3).forEach {
                delay(1000)
                emit(it)
            }
        }
        flow.collect { print(it) }
    }
    delay(2500)
    job.cancelAndJoin()
}

suspend fun testFlow5() {
    channelFlow {
        send(1)
        withContext(Dispatchers.IO) {
            send(2)
        }
    }

    listOf(1, 2, 3).asFlow()
    setOf(1, 2, 3).asFlow()
}

suspend fun testFlow6() {
    flow {
        List(100) {
            emit(it)
        }
    }.buffer()

    flow {
        List(100) {
            emit(it)
        }
    }.conflate().collect {}

    flow {
        List(100) {
            emit(it)
        }
    }.collectLatest { }
}

suspend fun testFlow7() {
    flow {
        List(5) {
            emit(it)
        }
    }.map {
        flow {
            List(it) {
                emit(it)
            }
        }
    }.flattenMerge().collect {
        print(it)
    }

}

