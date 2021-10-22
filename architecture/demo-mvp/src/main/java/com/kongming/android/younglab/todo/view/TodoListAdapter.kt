package com.kongming.android.younglab.todo.view

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kongming.android.younglab.R
import com.kongming.android.younglab.todo.bean.TodoItem
import com.kongming.parent.module.commonui.button.FlatButton
import com.kongming.parent.module.commonui.iconfont.IconFontView

/**
 * TodoList Adapter，继承自[BaseQuickAdapter]
 */
class TodoListAdapter(
    todoList: MutableList<TodoItem>
) : BaseQuickAdapter<TodoItem, BaseViewHolder>(
    R.layout.item_view,
    todoList
) {

    init {
        addChildClickViewIds(R.id.flatButton)
    }

    fun updateTodoList(todoList: List<TodoItem>) {
        setList(todoList)
    }

    fun updateTodoItem(todoItem: TodoItem) {
        val index = data.indexOf(todoItem)
        if (index >= 0) {
            setData(index, todoItem)
        }
    }

    override fun convert(holder: BaseViewHolder, item: TodoItem) {
        val iconFontView = holder.getView<IconFontView>(R.id.iconFontView)
        val textView = holder.getView<TextView>(R.id.textView)
        val flatButton = holder.getView<FlatButton>(R.id.flatButton)
        iconFontView.visibility = if (item.done) View.VISIBLE else View.INVISIBLE
        textView.text = item.title
        flatButton.text = if (item.done) "取消" else "完成"
    }
}