package com.ori.coroutines_base_learn.core.dispatcher

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object DefaultDispatcher : Dispatcher {

    /**
     * 声明一个线程组，取名DefaultDispatcher
     * */
    private val threadGroup = ThreadGroup("DefaultDispatcher")

    /**
     * 原子类
     * */
    private val threadIndex = AtomicInteger(0)

    /**
     * ThreadFactory: 输入runnable 返回一个属于DefaultDispatcher线程组的线程
     * 并设置为守护线程
     * */
    private val executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1) { runnable ->
            Thread(
                threadGroup,
                runnable,
                "${threadGroup.name}-worker-${threadIndex.getAndIncrement()}"
            ).apply { isDaemon = true }
        }

    /**
     * 分发，调用线程池的submit函数
     * */
    override fun dispatch(block: () -> Unit) {
        executor.submit(block)
    }
}