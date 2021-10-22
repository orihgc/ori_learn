package com.kongming.android.younglab.todo

import android.util.Log
import androidx.lifecycle.*
import com.kongming.android.younglab.base.request
import com.kongming.android.younglab.todo.bean.TodoItem
import com.kongming.android.younglab.todo.remote.TodoRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel，不要持有 Context
 */
class TodoListViewModel: ViewModel() {

    /**
     * 下划线表示私有，只有在 ViewModel 内部可改变值
     * Mutable 仅在内部使用
     */
    private val _todoItems: MutableLiveData<MutableList<TodoItem>> = MutableLiveData(mutableListOf())

    /**
     * 暴露给 View 的 LiveData
     * 外部只能观察，不可修改值。外部想要修改值只能通过 ViewModel 提供的方法
     */
    val todoItems: LiveData<MutableList<TodoItem>> = _todoItems

    /**
     * Loading 状态
     */
    private var _loading: MutableLiveData<Int> = MutableLiveData(STATUS_LOADING)

    val loadingStatus: LiveData<Int> = _loading

    /**
     * 兜底异常处理
     */
    private val errorHandler = CoroutineExceptionHandler { context, exception ->
        Log.e(TAG, "LoginViewModel normalLoginHandler Error occur!, context: $context", exception)
        _loading.value = STATUS_LOAD_ERROR
    }

    /**
     * 获取 todoItemList
     */
    fun fetchTodoList() {
        // ViewModel都是主线程
        viewModelScope.launch(Dispatchers.Main + errorHandler) {
            _loading.value = STATUS_LOADING
            try {
                _todoItems.value = request { TodoRepository.fetchTodoList() }.toMutableList()
                _loading.value = STATUS_LOADED
            } catch (e: Exception) {
                _loading.value = STATUS_LOAD_ERROR
            }
        }
    }

    /**
     * 修改某个 TodoItem 的完成状态
     */
    fun markItemDone(todoItem: TodoItem, done: Boolean) {
        // ViewModel都是主线程
        viewModelScope.launch(Dispatchers.Main) {
            _loading.value = STATUS_LOADING
            try {
                val done = request { TodoRepository.markItemDone(todoItem, done) }
                _todoItems.value = _todoItems.value?.mapTo(mutableListOf()) {
                    if (it.id == todoItem.id) it.copy(done = done)
                    else it
                }
                _loading.value = STATUS_LOADED
            } catch (e: Exception) {
                _loading.value = STATUS_LOAD_ERROR
            }
        }
    }

    companion object {
        const val TAG = "TodoListViewModel"

        const val STATUS_LOADING = 0
        const val STATUS_LOADED = 1
        const val STATUS_LOAD_ERROR = 2
    }
}