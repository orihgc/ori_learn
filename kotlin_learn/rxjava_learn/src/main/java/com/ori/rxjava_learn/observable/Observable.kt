package com.ori.rxjava_learn.observable

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


fun baseCase() {
    val observable = Observable.create(ObservableOnSubscribe<Int> {
        /**
         * ObservableEmitter是发射器的意思
         * 调用emitter的onNext onComplete onError onNext可以分别发送next事件、complete事件和error事件
         * 注意：
        上游可以发送无限个onNext, 下游也可以接收无限个onNext
        当上游发送了一个onComplete后, 上游onComplete之后的事件将会 继续 发送, 而下游收到 onComplete事件之后将 不再继续 接收事件
        当上游发送了一个onError后, 上游onError之后的事件将 继续 发送, 而下游收到onError事件之 后将 不再继续 接收事件.
        上游可以不发送onComplete或onError
        最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多 个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
         * */
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    })
    val observer = object : Consumer<Int> {
        override fun accept(t: Int?) {
            TODO("Not yet implemented")
        }

    }
    observable.map {
        println(it)
    }
}

/**
 * Disposable切断
 * */
fun disposableControl() {
    val observable = Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
        it.onNext(4)
    }).subscribe(object : Observer<Int> {

        /**
         * Disposable：一次性用品
         * 调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件
         * */
        private lateinit var mDisposable: Disposable
        private var i = 0


        override fun onComplete() {}
        override fun onSubscribe(d: Disposable) {
            if (d != null) {
                mDisposable = d
            }
        }

        override fun onNext(t: Int) {
            print("onNext $t")
            i++
            if (i == 2) {
                mDisposable.dispose()
            }
            println(t)
        }

        override fun onError(e: Throwable) {
        }

    })
}

/**
 * 不带任何参数的 subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可 不管你发什么.
 * 带有一个 Consumer 参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见, 因此我们如果只需要onNext事件可以这么写:
 * */
fun coldConsume() {
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onNext(4)
    }).subscribe({ t -> println(t) },
        { t -> print(t?.message) },
        { println("onComplete") },
        { println("subscribe") }
    )
}

/**
 * 热流publish
 * publish把冷流转为热流
 * */
fun hotPublishStream() {
    /**
     * publish
     * */
    val observable = Observable.create(ObservableOnSubscribe<Long> { emitter ->
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
            .take(Long.MAX_VALUE).subscribe {
                emitter.onNext(it)
            }
    }).publish()
    observable.connect()

    Thread.sleep(50)
    observable.subscribe {
        println(it)
    }

    Thread.sleep(500)
    observable.subscribe {
        println(it)
    }

    Thread.sleep(50000)
}

/**
 * 热流Subject
 *
 * 冷流转热流
 *
 * Subject既是Observable，又是Observer
 * 作为Observer，可以订阅目标cold Observable,使对方开始发送事件
 * 同事作为Observable转发或发送新的事件，让Cold Observable借助Subject转换为Hot Observable
 *
 * 不是线程安全的，需要调用 toSerialized方法
 * */
fun hotSubjectStream() {
    val observable = Observable.create(ObservableOnSubscribe<Long> { emitter ->
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation()).take(
            Long.MAX_VALUE
        ).subscribe { emitter.onNext(it) }
    })

    val publishSubject = PublishSubject.create<Long>()

    observable.subscribe(publishSubject)

    publishSubject.subscribe {
        println("a$it")
    }
    Thread.sleep(1000)

    publishSubject.subscribe {
        println("b$it")
    }

    Thread.sleep(2000)
}

/**
 * 热流转冷流
 *
 * refCount把一个ConnectableObservable转为一个普通的Observable
 * 当第一个订阅者/观察者订阅这个Observable时， RefCount连接到下层的可连接 Observable
 * RefCount跟踪有多少个观察者订阅它，直到最 后一个观察者完 成，才断开与下 层 可连接 Observable 的连接 。
 * */
fun hotToColdStream() {
    val connectableObservable = Observable.create(ObservableOnSubscribe<Long> { emitter ->
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
            .take(Long.MAX_VALUE).subscribe {
                emitter.onNext(it)
            }
    }).publish()

    connectableObservable.connect()

    val observable = connectableObservable.refCount()

    val consumer1 = Consumer<Long> { t -> println("a$t") }
    val consumer2 = Consumer<Long> { t -> println("b$t") }
    val consumer3 = Consumer<Long> { t -> println("c$t") }

    val disposableA = observable.subscribe(consumer1)
    Thread.sleep(50)
    val disposableB = observable.subscribe(consumer2)
    /** 如果还有订阅者没取消，那么consumer1和consumer2不会从头开始数据流*/
    //observable.subscribe(consumer3)
    Thread.sleep(100)

    disposableA.dispose()
    Thread.sleep(50)
    disposableB.dispose()

    println("重新开始数据流*************************")

    observable.subscribe(consumer1)
    Thread.sleep(50)
    observable.subscribe(consumer2)

    Thread.sleep(100)

}

/**
 * share操作符封装了 publish().refCount();
 * */
fun hotToClodWithShare() {
    val observable = Observable.create(ObservableOnSubscribe<Long> { emitter ->
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
            .take(Long.MAX_VALUE).subscribe {
                emitter.onNext(it)
            }
    }).share()

    val consumer1 = Consumer<Long> { t -> println("a$t") }
    val consumer2 = Consumer<Long> { t -> println("b$t") }

    val disposableA = observable.subscribe(consumer1)
    Thread.sleep(50)
    val disposableB = observable.subscribe(consumer2)
    Thread.sleep(100)

    disposableA.dispose()
    Thread.sleep(50)
    disposableB.dispose()

    println("重新开始数据流*************************")

    observable.subscribe(consumer1)
    Thread.sleep(50)
    observable.subscribe(consumer2)

    Thread.sleep(100)
}

fun main() {
    var count: Int = 0
    Observable.create(ObservableOnSubscribe<String> { emitter ->
        count++
        println("onCreate:  创造数字$count")
        emitter?.onNext("$count")
    }).flatMap {
        if (it.toInt() < 5) {
            Observable.error(Exception())
        } else {
            Observable.just(it)
        }
    }.retryWhen {
        it.flatMap { throwable ->
            if (count < 2) {
                Observable.timer(2, TimeUnit.SECONDS)
            } else {
                Observable.error(throwable)
            }
        }
    }
        .subscribe({
            println("subscribe: $it")
        }, {
            println("subscribe: $it")
        })
    Thread.sleep(100000)
}