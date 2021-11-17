package com.ori.fragment_learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.fragment_learn.fragment.Fragment1
import com.example.fragment_learn.master.ItemsListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("Activity", "onCreate Begin")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, Fragment1.newInstance("Hello Fragment1"), "f1")
        transaction.addToBackStack(null)
        transaction.replace(R.id.container, Fragment1.newInstance("Hello Fragment2"), "f2")
        transaction.commit()*/


        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, Fragment1.newInstance("Hello Fragment1"), "f1")
        transaction.addToBackStack(null)
//
        val colorButton = findViewById<Button>(R.id.button_1)
        colorButton.setOnClickListener {

            transaction.replace(R.id.container, Fragment1.newInstance("Hello Fragment2"), "f2")
            transaction.commit()

            startActivity(Intent(this, ItemsListActivity::class.java))
        }



        Log.e("Activity", "onCreate End")
    }

    override fun onStart() {
        Log.e("Activity", "onStart Begin")
        super.onStart()
        Log.e("Activity", "onStart End")

    }

    override fun onResume() {
        Log.e("Activity", "onResume Begin")
        super.onResume()
        Log.e("Activity", "onResume End")
    }

    override fun onPause() {
        Log.e("Activity", "onPause Begin")
        super.onPause()
        Log.e("Activity", "onResume End")
    }

    override fun onStop() {
        Log.e("Activity", "onStop Begin")
        super.onStop()
        Log.e("Activity", "onResume End")
    }

    override fun onDestroy() {
        Log.e("Activity", "onDestroy Begin")
        super.onDestroy()
        Log.e("Activity", "onDestroy End")
    }

}