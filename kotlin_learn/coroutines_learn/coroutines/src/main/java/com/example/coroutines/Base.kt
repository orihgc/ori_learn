package com.example.coroutines

import kotlinx.coroutines.*

/**
 * 协程是轻量级线程
 * */
fun first(){
    GlobalScope.launch {
        delay(1000)
        println("${Thread.currentThread().name}")
    }
    Thread.sleep(20000)
}

/**
 * runBlocking
 * */
fun blocking(){
    GlobalScope.launch {
        delay(1000)
        println("${Thread.currentThread().name}")
    }
    runBlocking {
        delay(2000)
        println("${Thread.currentThread().name}")
    }
    println("另一种写法")
    runBlocking {
        GlobalScope.launch {
            delay(1000)
            println("${Thread.currentThread().name}")
        }
        delay(2000)
        println("${Thread.currentThread().name}")
    }
    println("只有launch")
    runBlocking {
        launch {
            delay(1000)
            println("${Thread.currentThread().name}")
        }
        withContext(Dispatchers.IO){
            delay(1000)
            println("${Thread.currentThread().name}")
        }
        delay(2000)
        println("${Thread.currentThread().name}")
    }
}

/**
 * join
 * 等待一个协程运行结束
 * */
fun joining(){
    runBlocking {
        val job = GlobalScope.launch {
            delay(1000)
            println("${Thread.currentThread().name}")
        }
        job.join()
        println("${Thread.currentThread().name}")
    }
}

/**
 * coroutineScope
 * 作用域:
 * runBlocking和GlobalScope一样都是作用域构建器
 * coroutineScope可以声明自己的作用域，在所有已启动子协程执行完毕前不会结束
 * runBlocking和coroutineScope的区别：runBlocking会阻塞当前线程来等待，coroutineScope只是挂起
 * 所以coroutineScope是挂起函数
 *
 *
 * 执行顺序解释:
 *
 * coroutineScope：
 * coroutineScope是个挂起函数，它会将调度返回给外层runBlocking里的代码，而且是coroutineScope之上的代码，而非之下的代码
 * 之下的代码一定要等到coroutineScope执行完才能被执行,那么就会执行到launch里的withContext代码块里去.
 *
 * withContext
 * withContext是挂起函数，必须要执行完，才去执行它下面的代码
 * launch属于子协程，是并行执行的，不会阻塞withContext的执行。如果在withContext之前，那么和其顺序随机。
 * 如果在withContext之后，那么必定是先执行withContext。
 *
 * async
 * async和launch一样，是并行执行的。
 * 如果使用async{}.await，也是需要等结果返回，才去执行下面的代码。
 * */
fun coroutineScopeCase(){
    runBlocking {
        launch {
            withContext(Dispatchers.IO){
                delay(2000)
                println("launch withContext: ${Thread.currentThread().name}")
            }
            launch {
                delay(10)
                println("launch launch: ${Thread.currentThread().name}")
            }
        }
        coroutineScope {
            launch {
                println("coroutineScope launch1:${Thread.currentThread().name}")
            }
            withContext(Dispatchers.IO) {
                delay(1)
                println("coroutineScope withContext1:${Thread.currentThread().name}")
            }
            launch {
                println("coroutineScope launch2:${Thread.currentThread().name}")
            }
            withContext(Dispatchers.IO) {
                println("coroutineScope withContext2:${Thread.currentThread().name}")
            }
            launch {
                println("coroutineScope launch2:${Thread.currentThread().name}")
            }
            println("coroutineScope:${Thread.currentThread().name}")
        }

        println("runBlocking:${Thread.currentThread().name}")
    }
    println("ori")
}

fun main() {
}

