package com.ori.scene_learn.custom.widget

import androidx.lifecycle.ViewModel
import com.ori.scene_learn.custom.widget.model.SharedDataInfo

/**
 * Created by huangguocheng on 2024/2/6
 * @author huangguocheng@bytedance.com
 */
class WidgetViewModel : ViewModel() {

    val sharedDataInfo = SharedDataInfo()

    override fun onCleared() {
        super.onCleared()
    }
}