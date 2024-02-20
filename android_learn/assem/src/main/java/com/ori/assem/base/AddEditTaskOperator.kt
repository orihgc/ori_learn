package com.ori.assem.base

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class AddEditTaskOperator : TaskContentOperator {
    override suspend fun getTasks(): List<String>? {
        return emptyList()
    }
}