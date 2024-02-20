package com.ori.assem.base

import com.bytedance.assem.arch.viewModel.AssemViewModel

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class TaskContentVM : AssemViewModel<TaskContentState>() {

    override fun defaultState(): TaskContentState {
        return TaskContentState()
    }

    fun loadTasks() {
        setState {
            copy(empty = false)
        }
    }
}