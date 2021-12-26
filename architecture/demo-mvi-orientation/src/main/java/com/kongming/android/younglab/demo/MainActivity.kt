package com.kongming.android.younglab.demo

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kongming.android.younglab.R
import com.kongming.android.younglab.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect

class MainActivity : BaseActivity(), View.OnClickListener {

    val mainViewModel: MainViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun initData() {
        super.initData()
        initListener()
    }

    override fun initObserver() {
        super.initObserver()
        lifecycleScope.launchWhenStarted {
            mainViewModel.uiState.collect {
                tv.text = it.name
            }
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.view_id -> {
                mainViewModel.sendEvent(DemoContract.Event.ChangeText("Ori"))
            }
        }
    }

    private fun initListener() {
        view_id.setOnClickListener(this)
    }


}