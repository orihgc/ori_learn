package com.ori.fragment_learn.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ori.fragment_learn.R

class ItemDetailFragment : Fragment() {
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getSerializable("item") as Item?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.fragment_item_detail, container, false)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvBody = view.findViewById<TextView>(R.id.tvBody)
        tvTitle.text = item?.title
        tvBody.text = item?.body
        return view
    }

    companion object {
        fun newInstance(item: Item?): ItemDetailFragment {
            val fragmentDemo = ItemDetailFragment()
            val args = Bundle()
            args.putSerializable("item", item)
            fragmentDemo.arguments = args
            return fragmentDemo
        }
    }
}