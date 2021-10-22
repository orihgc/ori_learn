package com.kongming.android.younglab.todo.bean

/**
 * Todo事项的数据定义
 */
class TodoItem(
    /**
     * 标题
     */
    val title: String,

    /**
     * 是否完成
     */
    var done: Boolean
)