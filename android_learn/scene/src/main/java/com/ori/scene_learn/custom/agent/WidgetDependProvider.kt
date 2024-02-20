package com.ori.scene_learn.custom.agent

import java.util.concurrent.ConcurrentHashMap

object WidgetDependProvider {
    private val providers: MutableMap<Class<*>, WidgetContextDependProvider<*>> = ConcurrentHashMap()

    fun <T> registerService(clazz: Class<T>, t: T) {
        providers[clazz] = WidgetContextHolder(t)
    }

    fun <T> getService(clazz: Class<T>): T? {
        return providers[clazz]?.provideInstance() as T?
    }

}

interface WidgetContextDependProvider<out T> {
    fun provideInstance(): T?
    fun release()
}

class WidgetContextHolder<out T>(
    t: T?
) : WidgetContextDependProvider<T> {

    private var ref: T? = t

    override fun provideInstance(): T? = ref

    override fun release() {
        ref = null
    }
}