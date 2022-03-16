package com.ori.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.*


/**
 * 流构建器
 * */
fun createFlow(){
    flow {
        emit(1)
    }

    (1..10).asFlow()

    flowOf(1,2,3)
}

fun main() {
    val flow = flow<Int> {
        delay(100)
        emit(1)
    }

}