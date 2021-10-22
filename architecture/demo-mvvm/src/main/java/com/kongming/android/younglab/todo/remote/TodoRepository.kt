package com.kongming.android.younglab.todo.remote

import com.kongming.android.younglab.todo.bean.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

/**
 * 模拟项目中 RPC 提供的网络请求，例如 PB_Service
 */
object TodoRepository {

    /**
     * 获取 TodoItem 数据，sleep() 模拟网络请求阻塞
     */
    fun fetchTodoList(): List<TodoItem> {
        sleep(500)
        return listOf(
            TodoItem(1, "认识友善的新同事", true),
            TodoItem(2, "体验一下我们的App", true),
            TodoItem(3, "参加【Mini Project】并勇夺冠军", false)
        )
    }

    /**
     * 标记当前 Item 为 done，sleep() 模拟网络请求阻塞
     * @param done 已完成 / 未完成
     */
    fun markItemDone(todoItem: TodoItem, done: Boolean): Boolean {
        sleep(500)
        return done
    }
}