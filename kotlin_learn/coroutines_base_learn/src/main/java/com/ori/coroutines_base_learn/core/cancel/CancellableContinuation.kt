package com.ori.coroutines_base_learn.core.cancel

import com.ori.coroutines_base_learn.CancellationException
import com.ori.coroutines_base_learn.Job
import com.ori.coroutines_base_learn.OnCancel
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resumeWithException

/**
 * 包装一个Continuation，是一个静态代理的实现
 * 运用接口代理省掉对context的代理
 * */
class CancellableContinuation<T>(private val continuation: Continuation<T>) :
    Continuation<T> by continuation {

    /**
     * 取消状态
     * */
    private val state = AtomicReference<CancelState>(CancelState.InComplete)
    private val decision = AtomicReference(CancelDecision.UNDECIDED)

    val isCompleted: Boolean
        get() = when (state.get()) {
            CancelState.InComplete, is CancelState.CancelHandler -> false
            is CancelState.Complete<*>, CancelState.Cancelled -> true
        }

    /**
     * 表示挂起函数恢复执行
     * */
    override fun resumeWith(result: Result<T>) {
        when {
            decision.compareAndSet(CancelDecision.UNDECIDED, CancelDecision.RESUMED) -> {
                /**如果decision为UNDECIDED，表示挂起函数不会真正挂起，后续通过getResult同步返回结果*/
                state.set(CancelState.Complete(result.getOrNull(), result.exceptionOrNull()))
            }
            decision.compareAndSet(CancelDecision.SUSPENDED, CancelDecision.RESUMED) -> {
                /**否则只能是SUSPENDED，即挂起函数已挂起，需要在此处将异步结果返回*/
                state.updateAndGet {
                    when (it) {
                        is CancelState.Complete<*> -> {
                            throw IllegalStateException("Already completed.")
                        }
                        else -> {
                            CancelState.Complete(result.getOrNull(), result.exceptionOrNull())
                        }
                    }
                }
                /**返回异步结果*/
                continuation.resumeWith(result)
            }
        }
    }

    /**
     * 根据当前的挂起函数的执行状态给出结果
     * UNDECIDED：结果尚未就绪
     *
     * */
    fun getResult(): Any? {
        /**注册协程的取消回调*/
        installCancelHandler()

        if (decision.compareAndSet(CancelDecision.UNDECIDED, CancelDecision.SUSPENDED))
            /**返回挂起标志位COROUTINE_SUSPENDED*/
            return COROUTINE_SUSPENDED

        /**否则只能是RESUMED，即挂起函数没有真正挂起*/
        return when (val currentState = state.get()) {
            /**若未完成，返回挂起标志位*/
            is CancelState.CancelHandler, CancelState.InComplete -> COROUTINE_SUSPENDED
            CancelState.Cancelled -> throw CancellationException("Continuation is cancelled.")
            /***/
            is CancelState.Complete<*> -> {
                (currentState as CancelState.Complete<T>).let {
                    /**有异常，抛异常，不然返回结果*/
                    it.exception?.let { throw it } ?: it.value
                }
            }
        }
    }

    /**
     * 监听对应协程的取消事件*/
    private fun installCancelHandler() {
        if (isCompleted) return
        /**通过协程上下文来获取协程*/
        val parent = continuation.context[Job] ?: return
        parent.invokeOnCancel {
            doCancel()
        }
    }

    fun cancel() {
        if (isCompleted) return
        val parent = continuation.context[Job] ?: return
        parent.cancel()
    }

    /**
     * 注册取消回调
     * */
    fun invokeOnCancellation(onCancel: OnCancel) {
        val newState = state.updateAndGet {
            when (it) {
                /**InComplete转态即可注册回调*/
                CancelState.InComplete -> CancelState.CancelHandler(onCancel)
                /**多次注册抛出异常*/
                is CancelState.CancelHandler -> throw IllegalStateException("It's prohibited to register multiple handlers.")
                /**其他状态无响应*/
                is CancelState.Complete<*>, CancelState.Cancelled -> it
            }
        }
        if (newState is CancelState.Cancelled) {
            /**已经Cancelled状态，调用onCancel回调*/
            onCancel()
        }
    }

    private fun doCancel() {
        val prevState = state.getAndUpdate {
            when (it) {
                /**对于两种未完成状态，转变取消状态为Cancelled*/
                is CancelState.CancelHandler, CancelState.InComplete -> {
                    CancelState.Cancelled
                }
                CancelState.Cancelled, is CancelState.Complete<*> -> {
                    it
                }
            }
        }
        if (prevState is CancelState.CancelHandler) {
            /**若流转前有回调注册，就调用通知取消事件*/
            prevState.onCancel()
            resumeWithException(CancellationException("Cancelled."))
        }
    }
}


suspend inline fun <T> suspendCancellableCoroutine(
    crossinline block: (CancellableContinuation<T>) -> Unit
): T = suspendCoroutineUninterceptedOrReturn { continuation ->
    /**
     * continuation.intercepted()就是被拦截器拦截后的Continuation实例
     * 这里用CancellableContinuation替换掉SafeContinuation，表明它其实就是一个支持取消响应的Continuation
     * */
    val cancellable = CancellableContinuation(continuation.intercepted())
    block(cancellable)
    cancellable.getResult()
}