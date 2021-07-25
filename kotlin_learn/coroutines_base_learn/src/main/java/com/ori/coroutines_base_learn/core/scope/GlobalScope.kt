package com.ori.coroutines_base_learn.core.scope

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class GlobalScope : CoroutineScope {
    override val scopeContext: CoroutineContext
        get() = EmptyCoroutineContext
}