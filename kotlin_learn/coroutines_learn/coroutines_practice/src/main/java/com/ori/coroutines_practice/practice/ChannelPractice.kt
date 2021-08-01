package com.ori.coroutines_practice.practice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED


suspend fun main() {
    testChannel6()
}

suspend fun testChannel1() {
    val channel = Channel<Int>()
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            channel.send(i++)
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            val element = channel.receive()
            println(element)
        }
    }

    producer.join()
    consumer.join()
}

suspend fun testChannel2() {
    val channel = Channel<Int>(capacity = CONFLATED)
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            i++
            println("before send $i")
            channel.send(i)
            println("after send $i")
        }
    }

    val consumer = GlobalScope.launch {
        while (true) {
            delay(5000)
            val element = channel.receive()
            println("receive $element")
        }
    }

    producer.join()
    consumer.join()
}

suspend fun testChannel3() {
    val channel = Channel<Int>(capacity = UNLIMITED)
    val producer = GlobalScope.launch {
        var i = 0
        while (true) {
            delay(1000)
            i++
            println("before send $i")
            channel.send(i)
            println("after send $i")
        }
    }

//    val consumer = GlobalScope.launch {
//        val iterator = channel.iterator()
//        while (iterator.hasNext()){
//            println(iterator.next())
//        }
//    }

    val consumer2 = GlobalScope.launch {
        for (element in channel) {
            println(element)
        }
    }



    producer.join()
//    consumer.join()
    consumer2.join()
}

suspend fun testChannel4() {
    val receiveChannel: ReceiveChannel<Int> = GlobalScope.produce {
        repeat(100) {
            delay(1000)
            send(it)
        }
    }

    GlobalScope.launch {
        for (i in receiveChannel) {
            println(i)
        }
    }.join()


    val sendChannel: SendChannel<Int> = GlobalScope.actor<Int> {
        while (true) {
            val receive = receive()
            println(receive)
        }
    }


    GlobalScope.launch {
        for (i in 1..10)
            sendChannel.send(i)
    }.join()


}

suspend fun testChannel5() {
    val channel = Channel<Int>(3)

    GlobalScope.launch {
        List(3) {
            channel.send(it)
            println("send $it")
        }
        channel.close()
        println(
            """close channel.
          |  - ClosedForSend:${channel.isClosedForSend}
          |  - ClosedForReceive:${channel.isClosedForReceive}
        """.trimMargin()
        )
    }.join()

    GlobalScope.launch {
        for (element in channel) {
            println("receive $element")
            println(channel.isClosedForReceive)
            delay(1000)
        }
        println(
            """close channel.
          |  - ClosedForSend:${channel.isClosedForSend}
          |  - ClosedForReceive:${channel.isClosedForReceive}
        """.trimMargin()
        )
    }.join()
}

@ExperimentalCoroutinesApi
suspend fun testChannel6() {
    val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    GlobalScope.launch {
        List(3) {
            delay(5000)
            broadcastChannel.send(it)
            println("send $it")
        }
    }.join()

    List(3) { index ->
        GlobalScope.launch {
            val receiveChannel = broadcastChannel.openSubscription()
            for (i in receiveChannel) {
                println("[#$index] received: $i")
            }
        }.join()
    }
}