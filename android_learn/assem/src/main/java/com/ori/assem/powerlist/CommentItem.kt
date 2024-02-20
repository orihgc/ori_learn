package com.ori.assem.powerlist

import com.bytedance.ies.powerlist.data.PowerItem

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
data class CommentItem(
    val id: Int,
    val enable: Boolean,
    val content: String
) : PowerItem{

}