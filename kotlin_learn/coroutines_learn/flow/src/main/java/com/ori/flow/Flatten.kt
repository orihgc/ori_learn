package com.ori.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // 500
    emit("$i: Second")
}

/**
 * flatMapConcat
 * 连接模式，等待内部流完成之后开始收集下一个值
 * */
fun flatMapConcatFlow() = runBlocking {
    (1..3).asFlow().onEach { delay(100) }.flatMapConcat {
        requestFlow(it)
    }.collect {
        println(it)
    }
}

/**
 * flatMapMerge
 * 并发收集所有传入的流，并将他们的值合并到一个流中
 * */
fun flatMapMergeFlow()= runBlocking {
    (1..3).asFlow().onEach { delay(100) }.flatMapMerge { requestFlow(it) }.collect { println(it) }
}

/**
 * flatMapLatest
 * 在发出新流后，立即取消先前流的收集
 * */
fun flatMapLatestFlow()= runBlocking {
    (1..3).asFlow().onEach { delay(100) }.flatMapLatest { requestFlow(it) }.collect { println(it) }
}


fun main() {
    flatMapLatestFlow()
}