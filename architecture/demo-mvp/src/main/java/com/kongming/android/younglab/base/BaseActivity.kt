package com.kongming.android.younglab.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * MVP模式的Activity基类，实现BaseView。
 *
 * 实现以下一些能力：
 *   - V、P的范型定义
 *   - P的创建与依赖注入
 *   - P响应Activity生命周期
 *   - 抽象Activity页面创建、数据加载方法：[initData]、[initViews]、[initListeners]、[fetchData]
 *   - 实现Toast、Dialog等基本UI方法：[showToast]、[showLoadingDialog]、[hideLoadingDialog]
 */
abstract class BaseActivity<V : BaseView, P : BasePresenter<V>> :
    AppCompatActivity(), BaseView {

    protected lateinit var presenter: P

    //region 生命周期

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置页面UI

        val layoutId = getLayoutId()
        if (layoutId != View.NO_ID && layoutId != 0) {
            setContentView(layoutId)
        }

        // 初始化MVP，进行依赖注入

        presenter = createPresenter()
        presenter.attachView(this as V)

        // 数据加载、页面初始化

        initData()
        initViews()
        initListeners()
        fetchData()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    //endregion 生命周期

    //region 基本的UI方法

    override fun showToast(msg: String?) {
        msg?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showToast(id: Int) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingDialog(title: String) {
    }

    override fun hideLoadingDialog() {
    }

    //endregion 基本的UI方法

    //region MVP初始化

    protected abstract fun createPresenter(): P

    //endregion MVP初始化

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
     * 实现类重写这个方法进行Listener设置。
     */
    protected open fun initListeners() {
    }

    /**
     * 实现类重写这个方法获取数据，如发起网络请求。
     */
    protected open fun fetchData() {
    }

    //endregion 页面加载阶段
}