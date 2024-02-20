package com.ori.assem.powerlist.assem

import android.util.Log
import com.bytedance.ext_power_list.AssemPowerCell
import com.ori.assem.powerlist.CommentItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentCell : AssemPowerCell<CommentAssem, CommentItem>() {

    companion object {
        const val TAG = "CommentCell"
    }

    override fun createAssemAttached2Cell(): CommentAssem {
        Log.d(TAG,"createAssemAttached2Cell")
        return CommentAssem()
    }
}