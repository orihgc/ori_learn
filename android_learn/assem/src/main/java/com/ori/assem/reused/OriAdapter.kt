package com.ori.assem.reused

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.tiktok.proxy.IVMProxyHolder
import com.bytedance.tiktok.proxy.ReusedItem
import com.bytedance.tiktok.proxy.list.AssemReusedVHProxyer
import com.ori.assem.R

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class OriAdapter(val itemList: List<OriItem>, val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val proxy = AssemReusedVHProxyer<IVMProxyHolder<*, *>, ReusedItem>(this)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_comment_item, parent, false)
        return OriViewHolder(view, proxy, lifecycleOwner)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val oriViewHolder = holder as? OriViewHolder
        oriViewHolder?.onReusedBind(position, itemList[position])
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        val oriViewHolder = holder as? OriViewHolder
        oriViewHolder?.unReusedBind()
    }
}