package com.ori.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow



/**序列*/
fun learnSequence() {
    sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }.forEach {
        println(it)
    }
}

/**
 * 挂起函数
 * */
fun suspendSequence() {
    GlobalScope.launch {
        sequence {
            for (i in 1..3) {
                Thread.sleep(1000)
                yield(i)
            }
        }.forEach {
            println(it)
        }
    }
    Thread.sleep(3000)
}

/**
 * Flow流
 *
 * */
fun flowSimple() = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun learnFlow() = runBlocking {

    launch {
        for (i in 1..3) {
            println("I am not block :${Thread.currentThread().name}")
            delay(1000)
        }
    }
    flowSimple().collect {
        println("$it + ${Thread.currentThread().name}")
    }
}

/**
 * 流是冷的
 * */
fun clodFlow()= runBlocking {
    println("Calling simple function...")
    val flow = flowSimple()
    println("Calling collect...")
    flow.collect { value -> println(value) }
    println("Calling collect again...")
    flow.collect { value -> println(value) }
}

/**
 * 流的取消
 * 超时取消
 * */
fun cancelFlow()= runBlocking {
    withTimeout(250){
        flowSimple().collect { println(it) }
    }
    println("Done")
}

fun main() {
    cancelFlow()
}