package com.example.coroutines

import kotlinx.coroutines.*
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main() {
    lazyLaunch()
}

suspend fun doSomethingOne(): Int {
    delay(1000)
    return 2
}

suspend fun doSomethingTwo(): Int {
    delay(1000)
    return 2
}

fun doByOrder() = runBlocking {
    val measureNanoTime = measureTimeMillis {
        val doSomethingOne = doSomethingOne()
        val doSomethingTwo = doSomethingTwo()
    }
    println(measureNanoTime)
}

/**
 * async
 * 使用async进行并发
 *
 * async就类似于launch，它启动了一个单独的协程,这里会并发执行
 * */
fun asyncUse() = runBlocking {
    val time = measureTimeMillis {
        val one = async { doSomethingOne() }
        val two = async { doSomethingTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/**
 * 惰性启动
 * 将start参数设置为CoroutineStart.LAZY
 * 只有结果await时才会启动，或者Job的start函数调用时启动
 * 如果不调研start，只在println中调用await，就会导致顺序行为
 * */
fun lazyLaunch() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) {
            doSomethingOne()
        }
        val two = async(start = CoroutineStart.LAZY) {
            doSomethingTwo()
        }
        //
        one.start() //
        two.start() //
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/**
 * 异常处理
 * 如果其中一个子协程失败，其他的子协程和父协程都会被取消
 * */
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async {
        try {
            delay(Long.MAX_VALUE) //
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}
