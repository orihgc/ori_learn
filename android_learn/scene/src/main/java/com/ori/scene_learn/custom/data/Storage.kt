package com.ori.scene_learn.custom.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

/**
 * Created by huangguocheng on 2024/2/6
 * @author huangguocheng@bytedance.com
 */
class Storage {


    private val sharedStateMap = mutableMapOf<String, MutableStateFlow<Any>>()

    fun createSharedState(key: String, initValue: Any) {
        sharedStateMap[key] = MutableStateFlow(initValue)
    }

    suspend fun subscribe(key: String, callback: (Any) -> Unit) {
        sharedStateMap[key]?.collect {
            callback.invoke(it)
        }
    }

}