package com.example.coroutines_base_learn.core.dispatcher

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor

/**
 * 调度器接口
 * */
interface Dispatcher {
    /**分发方法*/
    fun dispatch(block: () -> Unit)
}

/**
 * 拦截器
 * 传入调度器
 * */
open class DispatcherContext(private val dispatcher: Dispatcher) :
    AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        DispatchedContinuation(continuation, dispatcher)
}

/**
 * 构建新的Continuation实例
 * 传调度器，调用调度器的dispatch函数
 * */
private class DispatchedContinuation<T>(val delegate: Continuation<T>, val dispatcher: Dispatcher) :
    Continuation<T> {
    override val context = delegate.context

    override fun resumeWith(result: Result<T>) {
        dispatcher.dispatch {
            delegate.resumeWith(result)
        }
    }
}