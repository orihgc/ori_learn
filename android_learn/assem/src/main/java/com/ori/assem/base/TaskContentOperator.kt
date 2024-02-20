package com.ori.assem.base

import com.bytedance.assem.arch.repository.RepositoryOperator

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
interface TaskContentOperator:RepositoryOperator {

    suspend fun getTasks():List<String>?
}