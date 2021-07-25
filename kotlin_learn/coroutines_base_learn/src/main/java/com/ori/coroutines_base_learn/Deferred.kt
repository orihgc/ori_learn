package com.ori.coroutines_base_learn

interface Deferred<T> : Job {
    suspend fun await(): T
}