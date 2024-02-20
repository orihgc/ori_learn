package com.ori.assem.powerlist.assem

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import com.bytedance.assem.arch.reused.IListItem
import com.bytedance.assem.arch.reused.ReusedUISlotAssem
import com.bytedance.assem.arch.viewModel.VMScope
import com.bytedance.ext_power_list.assemViewModel
import com.ori.assem.R
import com.ori.assem.powerlist.CommentItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentAssem : ReusedUISlotAssem<CommentAssem>(), IListItem<CommentItem> {

    companion object {
        const val TAG = "CommentAssem"
    }

    private val vm: CommentVM by assemViewModel(scope = VMScope.Holder)

    override fun contentLayoutId(): Int {
        return R.layout.cell_comment_item
    }

    override fun onViewCreated(view: View) {
        val textView = view.findViewById<TextView>(R.id.tv_item)
        Log.d(TAG, "onViewCreated ${textView.text}")
        vm.selectSubscribe(CommentState::enable) {
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

    override fun onBind(item: CommentItem) {
        Log.d(TAG, "onBind")
        val textView = contentView.findViewById<TextView>(R.id.tv_item)
        textView.text = item.content
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView() {
        super.onCreateView()
    }

    override fun onInActive() {
        super.onInActive()
        Log.d(TAG, "onInActive")
    }

    override fun onHostStart() {
        super.onHostStart()
        Log.d(TAG, "onHostStart")
    }

    override fun onHostResume() {
        super.onHostResume()
        Log.d(TAG, "onHostResume")
    }

    override fun onHostPause() {
        super.onHostPause()
        Log.d(TAG, "onHostPause")
    }

    override fun onHostStop() {
        super.onHostStop()
        Log.d(TAG, "onHostStop")
    }

    override fun onHostDestroy() {
        super.onHostDestroy()
        Log.d(TAG, "onHostDestroy")
    }

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)
        Log.d(TAG, "onViewAttached")
    }
}