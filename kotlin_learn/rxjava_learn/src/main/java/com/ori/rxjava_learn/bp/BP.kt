package com.ori.rxjava_learn.bp

import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

fun main() {
    bpCase()
}

/**
 * 背压
 * 背压必须是在异步的场景下才会出现。
 * */

/**
 * 案例
 * */
fun bpCase() {
    Observable.create(ObservableOnSubscribe<Int> {
        var i = 0
        while (true) {
            it.onNext(i++)
        }
    }).subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribe()
    Thread.sleep(90000)
}

/**
 * 控制速度，解决背压
 * */
fun ordinary() {
    val observable = Observable.create(ObservableOnSubscribe<Int> {
        var i = 0
        while (true) {
            it.onNext(++i)
            Thread.sleep(500)//从发送速度上解决
        }
    }).subscribeOn(Schedulers.io())
        /**过滤限流*/
        .sample(2, TimeUnit.SECONDS)/**在一段时间内，只处理最后一个数据*/
        .throttleFirst(2, TimeUnit.SECONDS)/**在一段时间内，只处理最后一个数据在一段时间内*/
        .debounce(2, TimeUnit.SECONDS)/**发送一个数据开始计时，到了规定时间。若没有再发送数据，则开始处理数据，反之重新开始计时*/
        /**打包缓存*/
        .buffer(3)/**将多个事件打包放入一个List中，再一起发射*/
        .window(3)/**将多个事件打包放入一个Observable中，再一起发射*/
        .observeOn(Schedulers.newThread()).subscribe {
            println(it)
        }
    Thread.sleep(100000)
}

/**
 * Flowable解决背压
 * RxJava2中才有的5种背压策略
 * BackpressureStrategy.MISSING 没有指定背压策略，需要下游指定背压操作符
 * BackpressureStrategy.ERROR 此策略表示，如果放入Flowable的异步缓存池中的数据超限了，则会抛出MissingBackpressureException异常
 * BackpressureStrategy.BUFFER,没有大小限制，可以放无数事件，不会抛异常，但会导致OOM
 * BackpressureStrategy.DROP 丢弃将要放进缓存池中的新的事件
 * BackpressureStrategy.LATEST 保留最新的事件，丢掉旧事件
 *
 * */
fun flowable(){

    Flowable.create<Int>({
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }, BackpressureStrategy.MISSING).subscribe(
        object : Subscriber<Int> {
            override fun onSubscribe(s: Subscription?) {
                /**
                 * 同步必须调用request
                 * 异步时，有一个128个事件的缓冲区
                 * */
                s?.request(10)//下游告诉上游我要处理几个
            }

            override fun onNext(t: Int?) {
            }

            override fun onError(t: Throwable?) {
            }

            override fun onComplete() {
            }

        }
    )

    Flowable.interval(10,TimeUnit.SECONDS)
        .onBackpressureBuffer(3)
        .onBackpressureDrop()
        .onBackpressureLatest()
}
