package com.ori.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce

/**
 * 管道是一种一个协程在流中开始生产可能无穷多个元素的模式
 * */
fun CoroutineScope.produceNumbers() = produce {
    var x = 1
    while (true) send(x++)
}

@ExperimentalCoroutinesApi
fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}

fun channelCase() = runBlocking {
    val numbers = produceNumbers()
    val square = square(numbers)
    repeat(5) {
        println(square.receive())
    }
    println("Done")
    coroutineContext.cancelChildren()
}

/**
 * 使用管道的素数
 * */
fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++)
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numbers) if (x % prime != 0) send(x)
}

fun prime() = runBlocking {
    var cur = numbersFrom(2)
    repeat(10) {
        val prime = cur.receive()
        println(prime)
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()
}

/**
 * 扇入
 * 多个协程可以发送到同一个通道
 * */
suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun fanIn() = runBlocking {
    val channel = Channel<String>()
    launch { sendString(channel,"foo",200) }
    launch { sendString(channel,"BAR!",500) }
    repeat(6){
        println(channel.receive())
    }
    coroutineContext.cancelChildren()
}

/**
 * 带缓冲的通道
 * */
fun bufferChannel()= runBlocking {
    val channel = Channel<Int>(1)
    val sender = launch {
        repeat(10) {
            println("Sending $it")
            channel.send(it)
        }
    }
    delay(1000)
    launch {
        repeat(10){
            println("Receive $it")
            channel.receive()
        }
    }
    sender.cancel()
}

fun main() {
    bufferChannel()
}