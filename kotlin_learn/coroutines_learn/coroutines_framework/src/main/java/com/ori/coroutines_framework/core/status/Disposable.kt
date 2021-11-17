package com.example.coroutines_base_learn.core.status

import com.example.coroutines_base_learn.Job
import com.example.coroutines_base_learn.OnCancel

interface Disposable {
    fun dispose()
}

/***/
typealias OnCompleteT<T> = (Result<T>) -> Unit

class CompletionHandlerDisposable<T>(val job: Job, val onComplete: OnCompleteT<T>) : Disposable {
    override fun dispose() {
        job.remove(this)
    }
}

/**
 * 移除回调
 * */
class CancellationHandlerDisposable(val job: Job, val onCancel: OnCancel) : Disposable {
    override fun dispose() {
        job.remove(this)
    }
}