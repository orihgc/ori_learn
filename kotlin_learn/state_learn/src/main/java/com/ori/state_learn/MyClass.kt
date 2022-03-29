package com.ori.state_learn

fun main() {
    val myClass = MyClass()
    println(myClass)
}

class MyClass{
    override fun toString(): String {
        return javaClass.name.toString()
    }
}