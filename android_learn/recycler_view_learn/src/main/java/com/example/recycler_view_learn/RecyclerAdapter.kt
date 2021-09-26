package com.example.recycler_view_learn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(var arrayData: ArrayList<ItemData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.textView?.text = arrayData[position].text
            holder.button?.text = arrayData[position].buttonText
        }
    }

    override fun getItemCount(): Int {
        return arrayData.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var button: Button? = null

        init {
            textView = itemView.findViewById(R.id.tv_test)
            button = itemView.findViewById(R.id.btn_test)
        }
    }
}