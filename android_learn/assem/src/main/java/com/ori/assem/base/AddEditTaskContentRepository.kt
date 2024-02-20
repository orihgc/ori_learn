package com.ori.assem.base

import com.bytedance.assem.arch.repository.AssemRepository

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class AddEditTaskContentRepository:AssemRepository<TaskContentOperator> {
    override val operator: TaskContentOperator
        get() = AddEditTaskOperator()

}