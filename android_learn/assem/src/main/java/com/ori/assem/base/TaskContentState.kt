package com.ori.assem.base

import com.bytedance.assem.arch.viewModel.VMState

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
data class TaskContentState(
    val empty: Boolean = true
) : VMState
