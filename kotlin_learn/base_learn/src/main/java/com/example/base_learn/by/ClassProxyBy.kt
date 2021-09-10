package com.example.base_learn.by

interface Base {
    fun print()
}

open class BaseImpl(val value: Int) : Base {
    override fun print() {
        print(value)
    }
}

class BaseImpl2(value2: Int): BaseImpl(value2) {
    override fun print() {
        print("hello")
    }
}

/**
 * by表示将base保存在Delegate对象实例内部，编译器生成继承自Base接口的所有方法，并将调用转发给b
 * */
class Delegate(val base: Base):Base by base

fun main() {
    val baseImpl = BaseImpl2(100)
    Delegate(baseImpl).print()
}