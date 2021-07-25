package com.ori.coroutines_base_learn.core.context

import kotlin.coroutines.CoroutineContext

class CoroutineName(val name: String) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<CoroutineName>

    override val key = Key

    override fun toString(): String {
        return name
    }
}