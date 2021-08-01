package com.ori.coroutines_practice.excetion

import kotlinx.coroutines.*
import java.io.InputStream
import kotlin.coroutines.coroutineContext

class CoroutineCancelInspection {

}

suspend fun main() {
    withContext(NonCancellable){
        delay(1000)
    }
}

