package com.ori.rxjava_learn.observable

import io.reactivex.*
import java.util.concurrent.TimeUnit


fun main() {
    completable()
}

/**
 * Single只有onSuccess和onError事件,且只能发送一个数据后续onSuccess会被忽略
 * */
fun single() {
    val single = Single.create(SingleOnSubscribe<Int> {
        it.onSuccess(1)
        it.onSuccess(2)
    }
    ).subscribe { t, u ->
        println(t)
    }
}

/**
 * Completable
 * 不会发送任何数据，只有onComplete和onError事件
 * 经常需要结合andThen操作符来使用，表示成功后的后续操作
 * */
fun completable(){
    val completable = Completable.create(CompletableOnSubscribe {
        TimeUnit.SECONDS.sleep(2)
        it.onComplete()
    }).andThen(Observable.range(1, 10)).subscribe {
        println(it)
    }
}

/**
 * Maybe
 * 这是Single和Completable的结合体
 * 也最多只能发送1个数据，多发送，会忽略
 * 如果 MaybeEmitter先调用了 onComplete()，即使后面再调用 onSuccess()，也不会发射任何 数据
 * */
fun maybe(){
    val maybe = Maybe.create(MaybeOnSubscribe<Int> {
        it.onSuccess(1)
        it.onComplete()
    }).subscribe {
        println(it)
    }
}