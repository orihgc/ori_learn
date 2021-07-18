package com.ori.coroutines_base_learn

import com.ori.coroutines_base_learn.core.coroutine.StandaloneCoroutine
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit): Job {
    val completion = StandaloneCoroutine(context)
    block.startCoroutine(completion)
    return completion
}