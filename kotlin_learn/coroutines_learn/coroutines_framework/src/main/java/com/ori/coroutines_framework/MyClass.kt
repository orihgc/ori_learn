package com.ori.coroutines_framework

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking


class MyClass {
}

suspend fun main() {
        coroutineScope {
            print("1")
        }
        print("2")
}