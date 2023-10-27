package com.ori.base_learn


import kotlin.reflect.KProperty

fun main() {
    register(Child::class.java)
}

open class Base{}
class Child: Base() {}

fun <T> register(clazz: Class<out T>){
    if (Base::class.java.isAssignableFrom(clazz)){
        val clazz1 = clazz as? Class<out Base>
        println("111")
    }
}

/**
 * 接口代理
 */
interface Api {
    fun print()
}


class ApiImpl(private val value: Int):Api{
    override fun print() {
        print(value)
    }
}

class ApiDelegate(b: Api) : Api by b

/**
 * 属性代理
 * 是对属性的get和set方法的代理
 * */
class MyDelegate<T>(val init: () -> T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return init()
    }
}

class M {
    val s: String by MyDelegate { "Hello" }
}


