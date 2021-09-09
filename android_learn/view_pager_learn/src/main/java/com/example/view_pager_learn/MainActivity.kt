package com.example.view_pager_learn

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.view_pager_learn.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val colorList = mutableListOf(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pageAdapter = PageAdapter()
        pageAdapter.setData(colorList)
        view_pager.adapter = pageAdapter
        view_pager.setPageTransformer(MarginPageTransformer(30))
    }
}