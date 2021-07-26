package com.ori.coroutines_base_learn

import com.ori.coroutines_base_learn.core.context.CoroutineName
import com.ori.coroutines_base_learn.core.coroutine.DeferredCoroutine
import com.ori.coroutines_base_learn.core.coroutine.StandaloneCoroutine
import com.ori.coroutines_base_learn.core.dispatcher.Dispatchers
import com.ori.coroutines_base_learn.core.scope.CoroutineScope
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

private var coroutineIndex = AtomicInteger(0)

fun CoroutineScope.launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): Job {
    val completion = StandaloneCoroutine(newCoroutineContext(context))
    block.startCoroutine(completion,completion)
    return completion
}

fun <T> CoroutineScope.async(context: CoroutineContext = Dispatchers.Default, block: suspend CoroutineScope.() -> T): Deferred<T> {
    val completion = DeferredCoroutine<T>(newCoroutineContext(context))
    block.startCoroutine(completion,completion)
    return completion
}

/**
 * 为协程创建一个默认的调度器
 * */
fun CoroutineScope.newCoroutineContext(context: CoroutineContext): CoroutineContext {
    val combined = context + CoroutineName("@coroutine#${coroutineIndex.getAndIncrement()}")
    /**
     * 如果调用者没有在协程上下文中主动配置调度器或者拦截器，就添加一个默认的调度器到协程上下文中
     * */
    return if (combined !== Dispatchers.Default && combined[ContinuationInterceptor] == null) combined + Dispatchers.Default else combined
}