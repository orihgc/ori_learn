package com.ori.coroutines_base_learn.core.status

import com.ori.coroutines_base_learn.Job
import com.ori.coroutines_base_learn.OnCancel

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

class CancellationHandlerDisposable(val job: Job, val onCancel: OnCancel) : Disposable {
    override fun dispose() {
        job.remove(this)
    }
}