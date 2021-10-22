package com.kongming.android.younglab.todo.view

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kongming.android.younglab.R
import com.kongming.android.younglab.base.BaseActivity
import com.kongming.android.younglab.todo.bean.TodoItem
import com.kongming.android.younglab.todo.presenter.TodoListPresenter
import kotlinx.android.synthetic.main.activity_todo_list.*

/**
 * 演示一个Todo-List页面
 */
class TodoListActivity : BaseActivity<TodoListView, TodoListPresenter>(), TodoListView {

    private var adapter: TodoListAdapter = TodoListAdapter(mutableListOf())

    override fun createPresenter() = TodoListPresenter()

    override fun getLayoutId(): Int = R.layout.activity_todo_list

    override fun initViews() {
        super.initViews()

        adapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.flatButton) {
                val item = this.adapter.data[position]
                // UI交互回调中调用Presenter处理业务逻辑
                presenter.markItemDone(item, !item.done)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun fetchData() {
        super.fetchData()
        presenter.loadTodoList()
    }

    override fun updateTodoList(todoItemList: List<TodoItem>) {
        adapter.updateTodoList(todoItemList)
    }

    override fun updateTodoItem(todoItem: TodoItem) {
        adapter.updateTodoItem(todoItem)
    }
}