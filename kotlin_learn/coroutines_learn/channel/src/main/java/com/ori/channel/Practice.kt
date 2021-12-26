package com.ori.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

fun main() {
    sharedFlowPractice()
}

/**
 * Channel和一个阻塞队列很像
 * 两个协程掉receive，就会竞争
 * */
fun channelPractice() = runBlocking {
    val channel = Channel<Int>()
    launch(Dispatchers.IO) {
        var i = 0
        while (true) {
            channel.send(i++)
            delay(500)
        }
    }
    launch {
        while (true) {
            println("A " +channel.receive())
        }
    }
    launch {
        delay(2000)
        while (true) {
            println("B " + channel.receive())
        }
    }
}

fun broadcastChannelPractice() = runBlocking {
    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    launch(Dispatchers.IO) {
        var i = 0
        while (true) {
            broadcastChannel.send(i++)
            delay(500)
        }
    }
    launch {
        while (true) {
            println("A " +broadcastChannel.openSubscription().receive())
        }
    }
    launch {
        delay(2000)
        while (true) {
            println("B " + broadcastChannel.openSubscription().receive())
        }
    }
}

fun sharedFlowPractice()= runBlocking {
    val mutableSharedFlow = MutableSharedFlow<Int>(4)
    launch(Dispatchers.IO) {
        var i = 0
        while (true) {
            mutableSharedFlow.emit(i++)
            delay(500)
        }
    }
    launch {
        mutableSharedFlow.collect {
            println("A $it")
        }
    }
    launch {
        delay(2000)
        mutableSharedFlow.collect {
            println("B $it")
        }
    }
}

fun stateFlowPractice()= runBlocking {
    val mutableStateFlow = MutableStateFlow<Int>(0)
    launch(Dispatchers.IO) {
        var i = 0
        while (true) {
            mutableStateFlow.value=i++
            delay(500)
        }
    }
    launch {
        mutableStateFlow.collect {
            println("A $it")
        }
    }
    launch {
        delay(2000)
        mutableStateFlow.collect {
            println("B $it")
        }
    }
}

