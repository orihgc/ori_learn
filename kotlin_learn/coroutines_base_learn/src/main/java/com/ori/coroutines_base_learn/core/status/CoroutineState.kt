package com.ori.coroutines_base_learn.core.status

sealed class CoroutineState {
    /**
     * 协程启动后立即进入InComplete状态，直到完成或者被取消
     * */
    class InComplete : CoroutineState()

    /**
     * 协程执行中被取消后进入该状态
     * 进入此状态后，要等待协程体内部的挂起函数调用响应取消
     * 响应后协程成功被取消，抛出CancellationException
     * */
    class Cancelling : CoroutineState()

    /**
     * 协程执行完成（包括正常返回或者异常返回）时进入该状态
     * */
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : CoroutineState()

    private var disposableList: RecursiveList<Disposable> = RecursiveList.Nil

    fun from(state: CoroutineState): CoroutineState {
        this.disposableList = state.disposableList
        return this
    }

    fun with(disposable: Disposable): CoroutineState {
        this.disposableList = RecursiveList.Cons(disposable, this.disposableList)
        return this
    }

    fun withOut(disposable: Disposable): CoroutineState {
        this.disposableList = this.disposableList.remove(disposable)
        return this
    }

    fun clear() {
        this.disposableList = RecursiveList.Nil
    }
}