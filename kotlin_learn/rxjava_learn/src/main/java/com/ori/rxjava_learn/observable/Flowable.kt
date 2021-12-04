package com.ori.rxjava_learn

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.function.Consumer

fun main() {

}

/**
 * Flowable
 * Observable一般处理最大不超过1000条数据
 * Flowable处理以某种方式超过10KB的元素
 * */
fun flowableCase() {
    /**
     * 如果采用BackpressureStrategy.BUFFER,没有大小限制，可以放无数事件
     * BackpressureStrategy.DROP丢弃存不下的事件
     * BackpressureStrategy.LATEST保留最新的事件
     * */
    Flowable.create<Int>({
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }, BackpressureStrategy.DROP).subscribe(
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
}

