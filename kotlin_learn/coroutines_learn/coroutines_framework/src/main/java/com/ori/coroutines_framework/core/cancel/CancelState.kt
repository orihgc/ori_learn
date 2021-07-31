package com.ori.coroutines_base_learn.core.cancel

import com.ori.coroutines_base_learn.OnCancel


sealed class CancelState {
    override fun toString(): String {
        return "CancelState.${this.javaClass.simpleName}"
    }

    /**
     * CancellableContinuation的取消回调只允许注册一个，因而不需要递归列表
     * 本质是携带了回调的InComplete状态
     * */
    class CancelHandler(val onCancel: OnCancel) : CancelState()
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CancelState()
    object Cancelled : CancelState()
    object InComplete : CancelState()
}