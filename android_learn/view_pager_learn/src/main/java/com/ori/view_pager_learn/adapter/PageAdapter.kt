package com.ori.view_pager_learn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.view_pager_learn.R

class PageAdapter : RecyclerView.Adapter<PageAdapter.PageHolder>() {

    private var mColorList = mutableListOf<Int>()


    class PageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView.findViewById(R.id.view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return PageHolder(itemView)
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        holder.view.setBackgroundColor(mColorList[position])
    }

    fun setData(colors: List<Int>) {
        mColorList.clear()
        if (colors.isNotEmpty()) {
            mColorList.addAll(colors)
        }
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int {
        return mColorList.size
    }
}