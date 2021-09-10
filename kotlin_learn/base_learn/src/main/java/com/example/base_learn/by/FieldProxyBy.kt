package com.example.base_learn.by

import kotlin.reflect.KProperty


class FieldDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef,这里委托了${property.name} 属性"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$thisRef 的 ${property.name} 属性赋值为 $value")
    }
}

class ProxyDemo {
    var value: String by FieldDelegate()
}

fun main() {
    val proxyDemo = ProxyDemo()
    println(proxyDemo.value)
    proxyDemo.value = "ori"
}

