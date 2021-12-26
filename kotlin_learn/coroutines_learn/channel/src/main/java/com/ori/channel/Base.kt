package com.ori.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun base() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (i in 1..5) channel.send(i)
    }
    repeat(5) {
        println(channel.receive())
    }
    println("Done")
}


fun closeChannel() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (i in 1..5) channel.send(i)
        channel.close()
    }
    //我们使用for循环打印所有被接收到的元素
    for (i in channel) println(i)
    println("Done")
}


/**
 * 协程构建器
 * */
fun consumeCase()= runBlocking {
    produce {
        for (x in 1..5) send(x * x)
    }.consumeEach { println(it) }
    println("Done")
}

fun main() {
    consumeCase()
}