package com.ori.messenger_learn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.ori.messenger_learn.databinding.ActivityMessengerBinding

class MessengerActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MessengerActivity"
        const val MESSAGE_FORM_CLIENT = 1001
        const val MESSAGE_SEND = 1002

        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MessengerService.MESSAGE_REPLAY -> {
                        Log.d(TAG, "${msg.data.getString("replay")}")
                    }
                }
            }
        }
    }

    private var mClientMessenger = Messenger(MessengerHandler())
    private var mServiceMessenger: Messenger? = null
    private lateinit var binding: ActivityMessengerBinding

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            mServiceMessenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            mServiceMessenger = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindService(Intent(this, MessengerService::class.java), mConnection, Context.BIND_AUTO_CREATE)
        binding.btnSend.setOnClickListener {
            kotlin.runCatching {
                mServiceMessenger?.send(Message.obtain(null, MESSAGE_SEND).apply {
                    replyTo = mClientMessenger
                    data = Bundle().apply {
                        putString("msg", "hello service")
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        unbindService(mConnection)
        super.onDestroy()
    }
}