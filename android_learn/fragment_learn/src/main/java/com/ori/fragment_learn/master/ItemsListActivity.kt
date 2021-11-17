package com.ori.fragment_learn.master

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.fragment_learn.R

class ItemsListActivity : FragmentActivity(), ItemsListFragment.OnItemSelectedListener {
    private var isTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        determinePaneLayout()
    }

    private fun determinePaneLayout() {
        val fragmentItemDetail = findViewById<FrameLayout>(R.id.flDetailContainer)
        if (fragmentItemDetail != null) {
            isTwoPane = true
            val fragmentItemsList: ItemsListFragment? =
                supportFragmentManager.findFragmentById(R.id.fragmentItemsList) as ItemsListFragment?
            fragmentItemsList?.setActivateOnItemClick(true)
        }
    }



    override fun onItemSelected(item: Item?) {
        if (isTwoPane) { // single activity with list and detail
            // Replace frame layout with correct detail fragment
            val fragmentItem = ItemDetailFragment.newInstance(item)
            val ft =
                supportFragmentManager.beginTransaction()
            ft.replace(R.id.flDetailContainer, fragmentItem)
            ft.commit()
        } else { // separate activities
            // launch detail activity using intent
            val i = Intent(this, ItemDetailActivity::class.java)
            i.putExtra("item", item)
            startActivity(i)
        }
    }
}