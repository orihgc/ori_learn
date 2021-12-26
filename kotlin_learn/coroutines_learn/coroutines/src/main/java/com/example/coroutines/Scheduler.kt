package com.example.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {

}

fun scheduleCase()= runBlocking {
    launch {
        println("运行在父协程的上下文中，即runBlocking的主协程")
    }
    launch(Dispatchers.Unconfined) {
        println("不受限的，将工作在主线程中")
    }
    launch(Dispatchers.Default) {
        println("将会获取默认调度器")
    }
    launch(newSingleThreadContext("MyOwnThread")) {
        println("获得一个新的线程")
    }
}