package com.example.coroutines_base_learn

import com.example.coroutines_base_learn.core.status.Disposable
import kotlin.coroutines.CoroutineContext


typealias OnComplete = () -> Unit

typealias CancellationException = java.util.concurrent.CancellationException
typealias OnCancel = () -> Unit

interface Job : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<Job>

    override val key: CoroutineContext.Key<*>
        get() = Job

    val isActive:Boolean

    fun cancel()

    suspend fun join()

    fun invokeOnCancel(onCancel: OnCancel): Disposable

    fun invokeOnCompletion(onComplete: OnComplete): Disposable

    fun remove(disposable: Disposable)

    fun attachChild(child: Job): Disposable
}