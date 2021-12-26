package com.ori.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis


suspend fun performRequest(request: Int): String {
    delay(1000)
    return "response $request"
}

/**
 * 映射map
 * 过滤filter
 * */
fun mapFlow() = runBlocking {
    (1..3).asFlow().map {
        performRequest(it)
    }.collect {
        println(it)
    }
}

/**
 * 转换操作符transform
 * */
fun transformFlow() = runBlocking<Unit> {
//sampleStart
    (1..3).asFlow() //
        .transform { request ->
            emit("Making request $request")
            emit(performRequest(request))
        }
        .collect { response -> println(response) }
//sampleEnd
}

/**
 * 限长操作符take
 * */
fun takeFlow() = runBlocking {
    flow {
        emit(1)
        emit(2)
        emit(3)
    }.take(2).collect {
        println(it)
    }
}

/**
 * 末端流操作符
 * single确保单个值发送
 * */
fun overOpt() = runBlocking {
    val toList = (1..5).asFlow().toList()
    val first = (1..5).asFlow().first()
    val single = (1..1).asFlow().single()

    val reduce = (1..5).asFlow().map { it * it }.reduce { accumulator, value ->
        accumulator + value
    }
    println(reduce)
}

/**
 * 流上下文
 * */
fun contextFlow() = runBlocking {
    flow {
        withContext(Dispatchers.IO) {
            emit(1)
            emit(2)
        }
    }.collect {
        println(1)
    }
}

fun flowOnFlow() = runBlocking {
    (1..5).asFlow().flowOn(Dispatchers.IO).collect {
        println(it)
    }
}

/**
 * 缓冲
 * */
fun bufferFlow() = runBlocking {
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.buffer()//没有缓冲区的话，只能处理一次发送一次。有buffer的话，可以先发射保存，后处理，速度更快
            .collect {
                delay(300)
                println(it)
            }
    }
    println(time)
}

/**
 * 合并，当收集器处理太慢时，可以跳过中间值
 * */
fun conflateFlow() = runBlocking {
    val time = measureTimeMillis {
        flow {
            for (i in 1..3) {
                delay(100)
                emit(i)
            }
        }.conflate()//跳过处理中间值，而只处理最新的那个
            .collect {
                delay(300)
                println(it)
            }
    }
    println(time)
}

/**
 * 处理最新值
 * conflate是通过删除发射值来实现
 * collectLatest取消缓慢的收集器，并在每次发射新值时重新启动它
 *              也就是说，如果新值到达，处理器还没有处理完上一个值，则取消，重新处理新值
 * */
fun collectLatestFlow() = runBlocking {
    val time = measureTimeMillis {
        flow {
            for (i in 1..10) {
                delay(299)
                emit(i)
            }
        }.collectLatest { value ->
            println("Collecting $value")
            delay(300) //            300
            println("Done $value")
        }
    }
    println(time)
}

/**
 * zip
 * 组合两个流
 * 同步等待
 * */
fun zipFlow() = runBlocking {
    (1..3).asFlow()
        .onEach { delay(300) }
        .zip(flowOf("a", "b", "c").onEach { delay(500) })
        { a, b ->
            "$a -> $b"
        }.collect { println(it) }
}

/**
 * combine
 * 此时，两个流（不管哪个流先发射）每次发射都会处理
 * */
fun combineFlow() = runBlocking {
    (1..3).asFlow()
        .onEach { delay(300) }
        .combine(flowOf("a", "b", "c").onEach { delay(400) })
        { a, b ->
            "$a -> $b"
        }.collect { println(it) }
}


fun main() {
    combineFlow()
}