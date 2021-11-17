package com.ori.notification_learn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var notificationManager: NotificationManager

    companion object{
        const val CHANNEL_ID="chat"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "聊天消息", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.btn_notify -> {
                val intent = Intent(this, NotificationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(this, 1, intent, 0)
                val builder = NotificationCompat.Builder(this, CHANNEL_ID).setAutoCancel(true)
                    .setContentTitle("收到聊天消息").setContentText("今天晚上吃什么？")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.parseColor("#F00606"))
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                notificationManager.notify(1,builder)
            }
        }
    }
}