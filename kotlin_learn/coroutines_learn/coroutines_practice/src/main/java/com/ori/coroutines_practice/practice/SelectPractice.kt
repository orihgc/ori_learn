package com.ori.coroutines_practice.practice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onReceiveOrNull
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlin.random.Random

suspend fun main() {

}

suspend fun testSelect1() {
    GlobalScope.launch {
        val deferred1 = async { }
        val deferred2 = async { }

        val select = select<Int> {
            deferred1.onAwait { 2 }
            deferred2.onAwait { 3 }
        }

        println(select)
    }.join()
}

suspend fun testSelect2() {
    val channels = List(10) { Channel<Int>() }

    GlobalScope.launch {
        delay(100)
        channels[Random.nextInt(10)].send(200)
    }

    val result = select<Int> {
        channels.forEach { channel ->
            channel.onReceive { it }
        }
    }

    List(100) { element ->
        select<Unit> {
            channels.forEach { channel ->
                channel.onSend(element) { sendChannel ->
                    println("sent on $sendChannel")
                }
            }
        }
    }
}

fun CoroutineScope.getDataFromLocal(string: String) = async {}
fun CoroutineScope.getDataFromNet(string: String) = async { }

suspend fun testSelect3() {
    coroutineScope {
        listOf(::getDataFromLocal, ::getDataFromNet).map {
            it.call("")
        }.map {
            flow { emit(it.await()) }
        }.merge()
            .onEach {
                println(it)
            }.launchIn(this)
    }
}

suspend fun testSelect4() {
    val channels = List(10) { Channel<Int>() }


    val result = channels.map {
        it.consumeAsFlow()
    }.merge()
        .first()
}