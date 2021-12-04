package com.ori.rxjava_learn.cut

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

fun main() {

}


/**
 * Transformer
 * 可以将一个Observable/Flowable/Single/Completable/Maybe 对象转换成另
 * 一 个Observable/Flowable/Single/Completable/Maybe 对象， 与调用一系列的内联操作符一模一样。
 * */
fun transformer(): ObservableTransformer<Int, String> {
    return ObservableTransformer { it ->
        it.map {
            "$it"
        }
    }
}

fun transformerCase() {
    val disposable = Observable.just(1, 2, 3).compose(transformer()).subscribe {
        println(it)
    }
}
