package com.ori.rxjava_learn.observable

import io.reactivex.Observable
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    intervalOpt()
}

/**
 * just发射指定值的Observable
 * 可接受1到10个参数
 * */
fun justOpt() {
    Observable.just(1, 2, 3, 4)
}

/**
 * from将其他种类的对象和数据类型转换为Observable
 * */
fun fromOpt() {

    Observable.fromArray(1, 2)

    Observable.fromIterable(ArrayList<Int>(mutableListOf()))

    val newCachedThreadPool = Executors.newCachedThreadPool()
    val future = newCachedThreadPool.submit(Callable<String> { "hello" })
    Observable.fromFuture(future)
}

/**
 * repeat
 * */
fun repeatOpt() {
    Observable.just(1).repeat(3)

    /**
     * repeatWhen有条件地重新订阅和发射原来的Observable
     * 将原始Observable的终止通知当做一个void数据传递给一个通知处理器
     * 通知处理器接受一个void，返回一个发射void的Observable
     *
     * 这里会过3s再发送一次0~8
     * */
    Observable.range(0, 9).repeatWhen {
        Observable.timer(3000, TimeUnit.MILLISECONDS)
    }

    /**
     * repeatUtil直到某个条件就不再重复发射数据
     * */
    val start = System.currentTimeMillis()
    Observable.interval(500, TimeUnit.MILLISECONDS).take(5)
        .repeatUntil {
            System.currentTimeMillis() - start > 7000
        }
        .subscribe {
            println(it)
        }

    Thread.sleep(10000)
}

/**
 * 一直等待直到有观察者订阅它
 * */
fun deferOpt(){
    val defer = Observable.defer {
        Observable.just(1, 2)
    }
    Thread.sleep(2000)
    defer.subscribe {
        println(it)
    }
}

/**
 * interval
 * */
fun intervalOpt(){
    Observable.interval(10,TimeUnit.MILLISECONDS)
}

/**
 * timer
 * */
fun timerOpt(){
    Observable.timer(100,TimeUnit.MILLISECONDS)
}