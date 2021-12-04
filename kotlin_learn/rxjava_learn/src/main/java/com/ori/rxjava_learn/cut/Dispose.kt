package com.ori.rxjava_learn.cut

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun main() {
    dispose()
}

fun dispose(){
    val disposable = Observable.just(1, 2, 3).subscribe {
        println(it)
    }
    disposable.dispose()
    /**disposable容器*/
    val compositeDisposable = CompositeDisposable().apply {
        add(disposable)
    }
    compositeDisposable.clear()
    Thread.sleep(5000)
}