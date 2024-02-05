package com.ori.scene_learn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by huangguocheng on 2024/2/5
 * @author huangguocheng@bytedance.com
 */
class SampleViewModel: ViewModel() {
    val counter: MutableLiveData<Int> = MutableLiveData()
}