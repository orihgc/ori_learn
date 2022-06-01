package com.ori.service_learn.fore

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForeService : Service() {

    private val mForegroundNF: ForegroundNF by lazy {
        ForegroundNF(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        mForegroundNF.startForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mForegroundNF.stopForegroundNotification()
        super.onDestroy()
    }


}