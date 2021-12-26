package com.ori.flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun simple(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) //
        } }
        .map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
        }

/**
 * catch只捕获上游异常
 * */
fun catchFlow()= runBlocking {
    simple().catch {
        emit("Caught on $it")
    }.collect { println(it) }
}

/**
 * finally
 * */
fun finallyFlow()= runBlocking {
    simple().onCompletion {
        if (it!=null) println()
        println("Done")
    }.collect {
        println(it)
    }


}

fun main() {

}