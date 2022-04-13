package learn.flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


/**
 * onStart
 * */
fun testOnStart() {
    runBlocking {
        (2..4).asFlow().onStart {
            emit(0)
            println("first onStart")
        }.map {
            it * it
        }.onStart {
            emit(1)
            println("second onStart")
        }.collect {
            println("collect $it")
        }
    }
}

/**
 * onCompletion
 * 在flow取消或结束时执行
 * 可以收集到上游流中的异常，但不会捕获
 * */
fun testOnCompletion() {
    runBlocking {
        (2..4).asFlow().onCompletion {
            emit(0)
            println("first onCompletion")
        }.map {
            it * it
        }.onCompletion {
            emit(1)
            println("second onCompletion")
        }.collect {
            println("collect $it")
        }
    }
}

/**
 * onEmpty
 * 当流完成时没有发射任务数据时回调，即在上游流完成时
 * */
fun testOnEmpty() {
    runBlocking {
        (2..4).asFlow().onEmpty {
            println("first onEmpty")
        }.filter {
            it < 1
        }.onEmpty {
            emit(-1)
            println("second onEmpty")
        }.collect {
            println("collect $it")
        }
    }
}

/**
 * catch捕获上游流中的异常
 * */
fun testCatch(){
    runBlocking {
        flow {
            emit(1)
            throw Exception("")
        }.catch{
            println("$it")
        }.collect{
            println(it)
        }
    }
}

/**
 * onEach
 * 捕获收集时的异常
 * */
fun testOnEach(){
    runBlocking {
        flow { emit(1) }.onEach {
            println(it)
            throw Exception("error on each")
        }.catch {
            println(it)
        }.collect()
    }
}

fun main() {
    testOnEach()
}