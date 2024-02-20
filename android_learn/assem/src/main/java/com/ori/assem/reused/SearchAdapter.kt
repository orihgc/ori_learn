package com.ori.assem.reused

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.tiktok.proxy.IVMProxyHolder
import com.bytedance.tiktok.proxy.ReusedItem
import com.bytedance.tiktok.proxy.list.AssemReusedVHProxyer

/**
 * Created by huangguocheng on 2024/2/20
 * @author huangguocheng@bytedance.com
 */
class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val proxy = AssemReusedVHProxyer<IVMProxyHolder<*, *>, ReusedItem>(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchVideoHolder = holder as SearchVideoHolder
        searchVideoHolder.onReusedBind(position, SearchVideoItem())
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

    }
}