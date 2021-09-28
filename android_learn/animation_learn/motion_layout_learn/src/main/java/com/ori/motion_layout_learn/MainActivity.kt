package com.ori.motion_layout_learn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sence.*

class MainActivity : AppCompatActivity() {
    private var isR=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sence)
        btn.setOnClickListener {
            if (isR){
                motionLayout.transitionToStart()
            }else{
                motionLayout.transitionToEnd()
            }
            isR=!isR
        }
    }
}