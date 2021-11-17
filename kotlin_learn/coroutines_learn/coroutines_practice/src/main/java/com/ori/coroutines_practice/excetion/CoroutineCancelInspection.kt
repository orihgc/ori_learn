package com.ori.coroutines_practice.excetion

import kotlinx.coroutines.*

class CoroutineCancelInspection {

}

suspend fun main() {
    withContext(NonCancellable){
        delay(1000)
    }
}

