package com.ori.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * 如果在onEach后collect
 * collect会一直等待直至流被收集
 * launchIn可以在单独的协程中启动流的收集，就可以立即继续进一步执行代码
 * */
fun launchFlow() = runBlocking {
    (1..3).asFlow().onEach { delay(100) }.onEach {
        println(it)
    }.collect()
    println("collect done")

    (1..3).asFlow().onEach { delay(100) }.onEach {
        println(it)
    }.launchIn(this)
    println("launchIn done")
}

/**
 * 流取消检测
 * */
fun cancelInspect() = runBlocking {
    flow {
        for (i in 1..5) {
            emit(i)
        }
    }.cancellable().collect {
        if (it == 3) cancel()
        println(it)
    }
}

fun main() {
    cancelInspect()
}