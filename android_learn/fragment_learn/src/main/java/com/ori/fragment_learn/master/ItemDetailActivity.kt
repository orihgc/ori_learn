package com.ori.fragment_learn.master

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ori.fragment_learn.R

class ItemDetailActivity : AppCompatActivity() {
    var fragmentItemDetail: ItemDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        val item = intent.getSerializableExtra("item") as Item?
        if (savedInstanceState == null) {
            fragmentItemDetail = ItemDetailFragment.newInstance(item)
            val ft =
                supportFragmentManager.beginTransaction()
            ft.replace(R.id.flDetailContainer, fragmentItemDetail!!)
            ft.commit()
        }
    }
}