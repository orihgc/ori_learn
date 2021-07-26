package com.ori.coroutines_base_learn.core.context

import kotlin.coroutines.CoroutineContext
/**
 * 为协程添加一个名称
 * */
class CoroutineName(val name: String) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<CoroutineName>

    override val key = Key

    override fun toString(): String {
        return name
    }
}