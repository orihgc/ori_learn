package com.kongming.android.younglab.todo.view

import com.kongming.android.younglab.base.BaseView
import com.kongming.android.younglab.todo.bean.TodoItem

/**
 * View接口定义
 */
interface TodoListView : BaseView {

    /**
     * 更新TodoList列表
     */
    fun updateTodoList(todoItemList: List<TodoItem>)

    /**
     * 更新一个[TodoItem]
     */
    fun updateTodoItem(todoItem: TodoItem)
}