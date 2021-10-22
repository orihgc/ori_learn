package com.kongming.android.younglab.todo

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kongming.android.younglab.R
import com.kongming.android.younglab.base.BaseActivity
import com.kongming.android.younglab.todo.bean.TodoItem
import kotlinx.android.synthetic.main.activity_todo_list.*

/**
 * 演示一个Todo-List页面
 */
class TodoListActivity : BaseActivity() {

    private var adapter: TodoListAdapter = TodoListAdapter(mutableListOf())

    /**
     * 如果在 Fragment 中，可以用 [Fragment.activityViewModels] 或者 [Fragment.viewModels]
     */
    private val viewModel: TodoListViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_todo_list

    override fun initViews() {
        super.initViews()

        adapter.setOnItemChildClickListener { _, view, position ->
            if (view.id == R.id.flatButton) {
                val item = this.adapter.data[position]
                // UI交互回调中调用Presenter处理业务逻辑
                viewModel.markItemDone(item, !item.done)
            }
        }
        adapter.setDiffCallback(object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean = oldItem === newItem

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun initObserver() {
        // 观察数据源
        viewModel.todoItems.observe(this) {
            adapter.updateTodoList(it)
        }
        // 观察 Loading 状态
        viewModel.loadingStatus.observe(this) {
            when (it) {
                TodoListViewModel.STATUS_LOADING -> showLoadingDialog("")
                TodoListViewModel.STATUS_LOADED, TodoListViewModel.STATUS_LOAD_ERROR -> hideLoadingDialog()
            }
        }
        // 如果在 Fragment 中 owner 建议传入 viewLifecycleOwner
    }

    override fun fetchData() {
        super.fetchData()
        viewModel.fetchTodoList()
    }
}