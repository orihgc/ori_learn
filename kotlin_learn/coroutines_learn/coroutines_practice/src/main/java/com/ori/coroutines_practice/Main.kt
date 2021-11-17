package com.ori.coroutines_practice

import kotlinx.coroutines.*

fun main(){
    val async = GlobalScope.launch {
        val res = fetch()
        print(res)
    }
     Thread.sleep(5000)
}

suspend fun fetch(): String = withContext(Dispatchers.IO) {
    delay(3000)
    "我"
}