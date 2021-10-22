package com.kongming.android.younglab.todo.presenter

import android.util.Log
import com.kongming.android.younglab.base.BasePresenter
import com.kongming.android.younglab.todo.bean.TodoItem
import com.kongming.android.younglab.todo.view.TodoListView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Presenter的实现类
 */
class TodoListPresenter : BasePresenter<TodoListView>() {

    companion object {
        private const val TAG = "TodoListPresenter"
    }

    /**
     * 使用[bindObservableLifeCycle]方法绑定页面生命周期
     */
    fun loadTodoList() {
        // 已经绑定了生命周期，无需处理Disposable
        val ignored = MockModel.loadTodoList()
            // 绑定页面生命周期
            .bindObservableLifeCycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showToast("加载中")
            }
            .subscribe(
                {
                    view.updateTodoList(it)
                },
                {
                    Log.e(TAG, it.message, it)
                    view.showToast("列表加载失败")
                },
                {
                    view.showToast("列表加载成功")
                }
            )
    }

    /**
     * 使用[bindObservableLifeCycle]方法绑定页面生命周期
     */
    fun markItemDone(todoItem: TodoItem, done: Boolean) {
        // 已经绑定了生命周期，无需处理Disposable
        val ignored = MockModel.markItemDone(todoItem, done)
            // 绑定页面生命周期
            .bindObservableLifeCycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showToast("更新中")
            }
            .subscribe(
                {
                    todoItem.done = done
                    view.updateTodoItem(todoItem)
                },
                {
                    Log.e(TAG, it.message, it)
                    view.hideLoadingDialog()
                    view.showToast("更新失败")
                },
                {
                    view.hideLoadingDialog()
                    view.showToast("更新成功")
                }
            )
    }

    object MockModel {

        fun loadTodoList(): Observable<List<TodoItem>> {
            return Observable
                .just(mockTodoList())
                .delay(1, TimeUnit.SECONDS)
        }

        fun markItemDone(todoItem: TodoItem, done: Boolean): Observable<Boolean> {
            val successful = true
            return Observable
                .just(successful)
                .delay(200, TimeUnit.MILLISECONDS)
        }

        private fun mockTodoList(): List<TodoItem> {
            return listOf(
                TodoItem("认识友善的新同事", true),
                TodoItem("体验一下我们的App", true),
                TodoItem("参加【Mini Project】并勇夺冠军", false)
            )
        }
    }
}