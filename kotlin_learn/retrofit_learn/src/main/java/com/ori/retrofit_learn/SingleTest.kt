package com.ori.retrofit_learn

fun main() {
    Child.test()
}

open class Parent {

    var name = "ori"

    fun printName() {
        println(name)
    }
}

object Child : Parent() {
    fun test() {
        printName()
    }
}