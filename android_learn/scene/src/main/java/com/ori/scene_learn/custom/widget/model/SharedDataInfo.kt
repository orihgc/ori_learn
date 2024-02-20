package com.ori.scene_learn.custom.widget.model

/**
 * Created by huangguocheng on 2024/2/6
 * @author huangguocheng@bytedance.com
 */
class SharedDataInfo {
    companion object {
        const val DEFAULT_STORAGE_ID = "default"
    }

    val storageId: String = DEFAULT_STORAGE_ID
    val sharedSateMap: Map<String, String> = mutableMapOf()
}
