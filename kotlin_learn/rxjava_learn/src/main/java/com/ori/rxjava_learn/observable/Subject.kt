package com.ori.rxjava_learn.observable

import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

fun main() {
    replaySubject()
}

/**
 * AsyncSubject: 无论在订阅前还是订阅后，只会收到onComplete之前的最后一条发射的数据
 * 必须要调用onComplete后，才会发送数据
 * */
fun asyncSubject() {
    val asyncSubject = AsyncSubject.create<Int>()
    asyncSubject.onNext(1)
    asyncSubject.onNext(2)
    asyncSubject.subscribe {
        println(it)
    }
    asyncSubject.onNext(3)
    asyncSubject.onNext(4)
    asyncSubject.onNext(5)
    asyncSubject.onComplete()

}

/**
 * BehaviorSubject：只会收到订阅前的最后一条数据以及订阅后的所有数据
 * 如果订阅前没有发送数据，则会发送一个默认数据
 * */
fun behaviorSubject() {
    val behaviorSubject = BehaviorSubject.createDefault(0)
    behaviorSubject.onNext(1)
    behaviorSubject.onNext(2)
    behaviorSubject.subscribe {
        println(it)
    }
    behaviorSubject.onNext(3)
    behaviorSubject.onNext(4)
}

/**
 * ReplaySubject会接受订阅前和订阅后的所有数据
 * */
fun replaySubject(){
    val replaySubject = ReplaySubject.create<Int>()
    replaySubject.onNext(1)
    replaySubject.onNext(2)
    replaySubject.subscribe {
        println(it)
    }
    replaySubject.onNext(3)
    replaySubject.onNext(4)

    /**修改缓存大小*/
    val replaySubjectWithSize = ReplaySubject.createWithSize<Int>(1)
    replaySubjectWithSize.onNext(1)
    replaySubjectWithSize.onNext(2)
    replaySubjectWithSize.onNext(3)
    replaySubjectWithSize.onNext(4)
    replaySubjectWithSize.subscribe {
        println(it)
    }

    /**限制缓存时间:createWithTime*/
}


/**
 * PublishSubject会接受订阅后的所有数据
 * */
fun test15(){
    val publishSubject = PublishSubject.create<Int>()
    publishSubject.onNext(1)
    publishSubject.onNext(2)
    publishSubject.subscribe{
        println(it)
    }
    publishSubject.onNext(3)
    publishSubject.onNext(4)
}