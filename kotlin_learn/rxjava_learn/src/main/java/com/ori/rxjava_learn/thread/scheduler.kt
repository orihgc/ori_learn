package com.ori.rxjava_learn

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

fun main() {
}


/**
 * subscribeOn: 通过接受一个Scheduler参数，来指定对数据的处理运行在特定的线程调度器上
 * 切多次执行subscribeOn，只有第一次有效
 *
 * observeOn：同样接受一个Scheduler参数，用来指定下游操作运行在特定的线程调度器Scheduler上
 * 若多次执行，每次都会起作用，线程会一直切换
 * */


/**
 * 每次都启用新线程，并在新线程中执行操作
 * */
fun newThread() {
    Observable.create(ObservableOnSubscribe<Int> {
        println(Thread.currentThread().name)
        it.onNext(1)
    }).subscribeOn(Schedulers.newThread())//指的是上游发送事件的线程,只有第一次调用生效
        .observeOn(Schedulers.newThread())//指的是下游接收事件的线程，每调用一次都生效
        .subscribe(
        object : Consumer<Int> {
            override fun accept(t: Int) {
                println(Thread.currentThread().name)
            }
        }
    )
    Thread.sleep(500)
}

/**
 * single
 * 使用定长为1的线程池，重复利用这个线程
 * */
fun singleThread(){
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onComplete()
    }).observeOn(Schedulers.single())
}

/**
 * io
 * 用于io密集型任务，支持异步阻塞IO操作
 * */
fun ioThread(){
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onComplete()
    }).observeOn(Schedulers.io())
}


/**
 * computation
 * 适用于CPU密集型的计算任务
 * */
fun computation(){
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onComplete()
    }).observeOn(Schedulers.computation())
}

/**
 * trampoline
 * 如果当前有线程在执行，将其暂停，等插入进来的新任务执行完成后，再接着执行原来未完成的任务
 * */
fun trampolineThread(){
    Observable.create(ObservableOnSubscribe<Int> {
        it.onNext(1)
        it.onComplete()
    }).observeOn(Schedulers.trampoline())
}


