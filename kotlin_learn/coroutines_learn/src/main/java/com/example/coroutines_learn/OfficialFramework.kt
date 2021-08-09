package com.example.coroutines_learn

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


/**
 * 启动模式
 * */
fun starMode() {
    GlobalScope.launch(Dispatchers.Main) {
        val result1 = withContext(Dispatchers.IO) {}
        val result2 = async(Dispatchers.IO) { }.await()
    }
}

/**
 * 调度器
 * */
class MyDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {

    }
}

suspend fun ExecutorsToDispatcher() {

}