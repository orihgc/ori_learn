package com.ori.rxjava_learn

import io.reactivex.Observable

fun main() {
    doLife()
}

fun doLife() {
    Observable.just(1, 2, 3).doOnSubscribe {
        println("doOnSubscribe：一旦观察者订阅了,就调用doOnSubscribe")
    }.doOnLifecycle({
        println("doOnLifecycle: ${it.isDisposed}")
    }, {
        println("doOnLifecycle:可在观察者订阅之后，设置是否取消订阅")
    }).doOnNext {
        println("doOnNext: 在onNext之前调用")
    }.doAfterNext {
        println("doAfterNext：在onNext执行后执行")
    }.doOnEach {
        println("doOnEach:产生的Observable每发射一次数据就调用一次,包括onNext、onError、onComplete")
    }.doOnComplete {
        println("doOnComplete:当它产生的Observable正常终止调用onComplete时调用")
    }.doOnError {
        println("doOnError:当它产生的Observable异常终止调用onError时调用")
    }.doOnTerminate {
        println("doOnTerminate:当它产生的Observable调用OnComplete或者onError时调用")
    }.doFinally {
        println("doFinally:终止后调用，无论异常还是正常，优先于doAfterTerminate")
    }.doAfterTerminate {
        println("doAfterTerminate:当它产生的Observable调用OnComplete或者onError后触发")
    }.subscribe {
        println(it)
    }

}