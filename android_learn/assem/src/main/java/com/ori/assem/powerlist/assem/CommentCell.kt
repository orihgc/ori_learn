package com.ori.assem.powerlist.assem

import com.bytedance.ext_power_list.AssemPowerCell
import com.ori.assem.powerlist.CommentItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class CommentCell : AssemPowerCell<CommentAssem, CommentItem>() {
    override fun createAssemAttached2Cell(): CommentAssem {
        return CommentAssem()
    }
}