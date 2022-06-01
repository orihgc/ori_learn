package com.ori.service_learn.bind

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class MyService : Service() {
    companion object {
        const val TAG = "MyService"
    }

    inner class MyBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

    private val binder = MyBinder()
    private val mGenerator= Random()

    val randomNum:Int
        get() = mGenerator.nextInt(100)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG,"onBind")
        return binder
    }
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"onUnbind")
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestory")
    }

}