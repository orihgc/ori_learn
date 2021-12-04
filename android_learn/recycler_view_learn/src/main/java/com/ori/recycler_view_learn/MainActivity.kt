package com.ori.recycler_view_learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycler_view_learn.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerAdapter = RecyclerAdapter(initData())
        rv_recycler.layoutManager = LinearLayoutManager(this)
        rv_recycler.adapter = recyclerAdapter
    }

    private fun initData(): ArrayList<ItemData> {
        var listData = ArrayList<ItemData>()
        listData.add(ItemData("test1", "测试1"))
        listData.add(ItemData("test2", "测试2"))
        listData.add(ItemData("test3", "测试3"))
        listData.add(ItemData("test4", "测试4"))
        return listData
    }
}