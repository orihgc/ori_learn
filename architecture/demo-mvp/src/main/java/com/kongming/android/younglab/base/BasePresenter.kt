package com.kongming.android.younglab.base

import androidx.lifecycle.Lifecycle
import com.kongming.android.younglab.base.lifecycle.RxLifecycle
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * MVP模式的Presenter基类。
 * Presenter实现业务逻辑，操作[view]更新界面。
 *
 * 实现以下一些能力：
 *   - V的范型定义
 *   - 响应Activity生命周期
 *   - 定义RxJava扩展方法绑定页面生命周期：[bindObservableLifeCycle]
 */
open class BasePresenter<V : BaseView> {

    protected lateinit var view: V

    /**
     * 用于响应页面生命周期
     */
    private val lifecycleBehavior = BehaviorSubject.createDefault(Lifecycle.Event.ON_CREATE)

    /**
     * View依赖注入，并开始页面生命周期
     */
    fun attachView(view: V) {
        this.view = view
        lifecycleBehavior.onNext(Lifecycle.Event.ON_CREATE)
    }

    /**
     * 结束页面生命周期
     */
    fun detachView() {
        lifecycleBehavior.onNext(Lifecycle.Event.ON_DESTROY)
    }

    /**
     * [Observable]绑定页面生命周期的的扩展方法
     */
    open fun <T> Observable<T>.bindObservableLifeCycle(): Observable<T> {
        return this.compose(
            RxLifecycle.bindUntilEvent(
                lifecycleBehavior,
                Lifecycle.Event.ON_DESTROY
            )
        )
    }

    /**
     * [Single]绑定页面生命周期的的扩展方法
     */
    open fun <T> Single<T>.bindObservableLifeCycle(): Single<T> {
        return this.compose(
            RxLifecycle.bindUntilEvent(
                lifecycleBehavior,
                Lifecycle.Event.ON_DESTROY
            )
        )
    }

    /**
     * [Flowable]绑定页面生命周期的的扩展方法
     */
    open fun <T> Flowable<T>.bindObservableLifeCycle(): Flowable<T> {
        return this.compose(
            RxLifecycle.bindUntilEvent(
                lifecycleBehavior,
                Lifecycle.Event.ON_DESTROY
            )
        )
    }

    /**
     * [Maybe]绑定页面生命周期的的扩展方法
     */
    open fun <T> Maybe<T>.bindObservableLifeCycle(): Maybe<T> {
        return this.compose(
            RxLifecycle.bindUntilEvent(
                lifecycleBehavior,
                Lifecycle.Event.ON_DESTROY
            )
        )
    }
}