package com.ori.assem.reused

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import com.bytedance.assem.arch.reused.IListItem
import com.bytedance.assem.arch.reused.ReusedUIContentAssem
import com.bytedance.assem.arch.viewModel.VMScope
import com.bytedance.ext_power_list.assemViewModel
import com.ori.assem.R
import com.ori.assem.powerlist.assem.CommentAssem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */

class OriRootAssem : ReusedUIContentAssem<OriRootAssem>(), IListItem<OriItem> {


    private val vm: OriAssemVM by assemViewModel(scope = VMScope.Holder)


    override fun onViewCreated(view: View) {
        val textView = view.findViewById<TextView>(R.id.tv_item)
        Log.d(CommentAssem.TAG, "onViewCreated ${textView.text}")
        vm.selectSubscribe(OriState::enable) {
            if (it) {
                textView.setBackgroundColor(Color.RED)
            } else {
                textView.setBackgroundColor(Color.WHITE)
            }
        }
        textView.setOnClickListener {
            vm.click()
        }
    }

    override fun onBind(item: OriItem) {
        Log.d(CommentAssem.TAG, "onBind")
        val textView = containerView.findViewById<TextView>(R.id.tv_item)
        textView.text = item.id.toString()
    }
}