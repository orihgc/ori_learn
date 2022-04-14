package com.ori.aspectj_learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ori.aspectj_learn.aspect.ALog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1.setOnClickListener {
            onClickTest()
        }
    }

    @ALog(value = "onCreate ori")
    fun onClickTest() {
        Log.i(this.javaClass.simpleName, "onClickTest")
    }
}