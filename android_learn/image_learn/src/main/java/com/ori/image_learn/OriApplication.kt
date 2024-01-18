package com.ori.image_learn

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco

class OriApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("OriApplication","OriApplication")
        Fresco.initialize(this)
    }
}