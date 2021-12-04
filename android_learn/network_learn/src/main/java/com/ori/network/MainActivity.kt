package com.ori.network

import android.net.TrafficStats
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uidRxBytes = TrafficStats.getUidRxBytes(Process.myUid())
        Log.d("tag","$uidRxBytes")
    }
}