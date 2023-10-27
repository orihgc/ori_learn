package com.ori.messenger_learn

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class MessengerService : Service() {

    companion object {
        const val TAG = "MessengerService"

        const val MESSAGE_REPLAY = 1003

        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    MessengerActivity.MESSAGE_FORM_CLIENT -> {
                        Log.d(TAG, "${msg.data.getString("msg")}")
                    }

                    MessengerActivity.MESSAGE_SEND -> {
                        Log.d(TAG, "${msg.data.getString("msg")}")
                        msg.replyTo.send(Message.obtain(null, MESSAGE_REPLAY).apply {
                            data = Bundle().apply {
                                putString("replay", "hello client")
                            }
                        })
                    }

                    else -> {
                        super.handleMessage(msg)
                    }
                }
            }
        }
    }

    private val messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }
}