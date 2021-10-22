package com.kongming.android.younglab.base.lifecycle

import io.reactivex.Observable

/**
 * 处理页面生命周期绑定。
 */
object RxLifecycle {

    fun <T, E> bindUntilEvent(lifecycle: Observable<E>, event: E): LifecycleTransformer<T> {
        return bind(takeUntilEvent(lifecycle, event))
    }

    private fun <T, E> bind(lifecycle: Observable<E>): LifecycleTransformer<T> {
        return LifecycleTransformer(lifecycle)
    }

    private fun <E> takeUntilEvent(lifecycle: Observable<E>, event: E): Observable<E> {
        return lifecycle.filter { lifecycleEvent -> lifecycleEvent == event }
    }
}