package com.ori.state_machine

import android.os.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val hsm = HSM("rtc")
        hsm.sendMessage(HSM.MSG_P0_P1)
    }
}