package com.ori.scene_learn.custom.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by huangguocheng on 2024/2/6
 * @author huangguocheng@bytedance.com
 */
object SharedDataManager {


    private val storageMap = mutableMapOf<String, Storage>()
    private val storageScopeMap = mutableMapOf<String, CoroutineScope>()

    fun addSharedDataItem(storageId: String, key: String, initValue: Any) {
        if (storageMap.contains(storageId)) {
            storageMap[storageId]?.createSharedState(key, initValue)
        } else {
            storageMap[storageId] = Storage().apply {
                createSharedState(key, initValue)
            }
            storageScopeMap[storageId] = CoroutineScope(Dispatchers.IO)
        }
    }

    fun subscribeSharedData(storageId: String, key: String, callback: (Any) -> Unit) {
        if (!storageMap.contains(storageId) || !storageScopeMap.contains(storageId)) return
        storageScopeMap[storageId]?.launch {
            storageMap[storageId]?.subscribe(key, callback)
        }
    }


}