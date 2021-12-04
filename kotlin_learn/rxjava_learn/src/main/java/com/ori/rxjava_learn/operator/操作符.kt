package com.ori.rxjava_learn

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun main() {
}

/**
 * 映射
 * */
fun test5() {
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
    }).map {
        "This result $it"
    }.subscribe(object : Consumer<String> {
        override fun accept(t: String?) {
            println(t)
        }

    })
}

/**
 * flatMap
 * 上游每发送一个事件, flatMap都将创建一个新的水管, 然后发送转换之后的新的事件, 下游接收到的 就是这些新的水管发送的数据. 这里需要注意的是, flatMap并不保证事件的顺序
 * */
fun test6() {
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
    }).flatMap {
        val mutableList = mutableListOf<String>()
        for (i in 1..3) {
            mutableList.add("I am value $it")
        }
        Observable.fromIterable(mutableList).delay(10, TimeUnit.MILLISECONDS)
    }.subscribe(object : Consumer<String> {
        override fun accept(t: String?) {
            println(t)
        }

    })
    Thread.sleep(500)
}

/**
 * concatMap有序
 * */
fun test7(){
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
    }).concatMap {
        val mutableList = mutableListOf<String>()
        for (i in 1..3) {
            mutableList.add("I am value $it")
        }
        Observable.fromIterable(mutableList).delay(10, TimeUnit.MILLISECONDS)
    }.subscribe(object : Consumer<String> {
        override fun accept(t: String?) {
            println(t)
        }

    })
    Thread.sleep(500)
}

/**
 * zip事件合并
 * */
@Suppress("CheckResult")
fun test8(){
    //先发送第一个observable1
    val observable1 = Observable.create(ObservableOnSubscribe<Int> {
        print("emit 1")
        it.onNext(1)
        print("emit 2")
        it.onNext(2)
        it.onComplete()
        print("emit 3")
        it.onNext(3)
    }).subscribeOn(Schedulers.io())
    //再发送第二个observable
    val observable2 = Observable.create(ObservableOnSubscribe<String> {
        print("emit A")
        it.onNext("A")
        print("emit B")
        it.onNext("B")
        print("emit C")
        it.onNext("C")
    }).subscribeOn(Schedulers.io())


    Observable.zip(observable1,observable2,
        { t1, t2 -> "$t1" + t2 }).subscribe(object :Consumer<String>{
        override fun accept(t: String?) {
            println(t)
        }

    })
    Thread.sleep(500)
}

