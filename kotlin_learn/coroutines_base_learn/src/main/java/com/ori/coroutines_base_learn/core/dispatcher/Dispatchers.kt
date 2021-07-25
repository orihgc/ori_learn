package com.ori.coroutines_base_learn.core.dispatcher

object Dispatchers {

    val Default by lazy {
        DispatcherContext(DefaultDispatcher)
    }

    val Android by lazy {
        DispatcherContext(AndroidDispatcher)
    }
}