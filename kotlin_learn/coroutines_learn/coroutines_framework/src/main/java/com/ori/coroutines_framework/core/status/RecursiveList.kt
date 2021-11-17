package com.example.coroutines_base_learn.core.status


/**
 * 循环列表
 * */
sealed class RecursiveList<out T> {
    object Nil : RecursiveList<Nothing>()

    class Cons<T>(val head: T, val tail: RecursiveList<T>) : RecursiveList<T>()
}

/**
 * 删除循环列表中的某项
 * */
fun <T> RecursiveList<T>.remove(element: T): RecursiveList<T> {
    return when (this) {
        RecursiveList.Nil -> this
        is RecursiveList.Cons -> {
            if (head == element) {
                return tail
            } else {
                RecursiveList.Cons(head, tail.remove(element))
            }
        }
    }
}

/**
 * 遍历循环列表
 * tailrec：编译器会优化该递归，变成快速高效的循环的版本
 * */
tailrec fun <T> RecursiveList<T>.forEach(action: (T) -> Unit): Unit = when (this) {
    RecursiveList.Nil -> Unit
    is RecursiveList.Cons -> {
        action(this.head)
        this.tail.forEach { action }
    }
}

/**
 * reified：具象类型
 * */
inline fun <reified T> RecursiveList<Any>.loopOn(crossinline action: (T) -> Unit) = forEach {
    when (it) {
        is T -> action(it)
    }
}