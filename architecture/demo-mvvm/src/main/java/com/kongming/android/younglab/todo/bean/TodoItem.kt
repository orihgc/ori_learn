package com.kongming.android.younglab.todo.bean

/**
 * Todo事项的数据定义
 */
data class TodoItem(

    /**
     * id
     */
    val id: Long,

    /**
     * 标题
     */
    val title: String,

    /**
     * 是否完成
     */
    val done: Boolean
)