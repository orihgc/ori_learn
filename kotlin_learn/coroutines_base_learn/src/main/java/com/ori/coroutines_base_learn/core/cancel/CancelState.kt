package com.ori.coroutines_base_learn.core.cancel

import com.ori.coroutines_base_learn.OnCancel


sealed class CancelState {
    override fun toString(): String {
        return "CancelState.${this.javaClass.simpleName}"
    }

    object InComplete : CancelState()
    class CancelHandler(val onCancel: OnCancel) : CancelState()
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CancelState()
    object Cancelled : CancelState()
}