package com.kongming.android.younglab.base.lifecycle

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * 使用[ObservableTransformer]和[Observable.takeUntil]方法实现"直到页面生命周期结束前"的能力。
 * [observable]为页面生命周期结束事件流。
 */
class LifecycleTransformer<T>(
    private val observable: Observable<*>
) : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.takeUntil(observable)

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.takeUntil(observable.firstOrError())

    override fun apply(upstream: Maybe<T>): MaybeSource<T> =
        upstream.takeUntil(observable.firstElement())
}