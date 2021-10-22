package com.kongming.android.younglab.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置页面UI
        val layoutId = getLayoutId()
        if (layoutId != View.NO_ID && layoutId != 0) {
            setContentView(layoutId)
        }

        // 数据加载、页面初始化
        initData()
        initViews()
        initObserver()
        fetchData()
    }
    //endregion 生命周期

    //region 基本的UI方法

    fun showToast(msg: String?) {
        msg?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(id: Int) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }

    open fun showLoadingDialog(title: String) {}

    open fun hideLoadingDialog() {}

    //endregion 基本的UI方法

    //region 页面加载阶段

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 实现类重写这个方法处理本地的数据，如获取[Intent]里的数据。
     */
    protected open fun initData() {
    }

    /**
     * 实现类重写这个方法进行View的初始化。
     */
    protected open fun initViews() {
    }

    /**
     * 观测对象
     */
    protected open fun initObserver() {
    }

    /**
     * 实现类重写这个方法获取数据，如发起网络请求。
     */
    protected open fun fetchData() {
    }

    //endregion 页面加载阶段
}