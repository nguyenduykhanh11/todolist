package com.example.todolist

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val notifyID = 1
const val channelID = "channel"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val timeExtra = "12:00"
class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification: Notification? = context?.let {
            NotificationCompat.Builder(it, channelID)
                .setSmallIcon(R.drawable.ic_arrow_back)
                .setContentTitle(intent?.getStringExtra(titleExtra))
                .setContentText(intent?.getStringExtra(messageExtra))
                .build()
        }

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notifyID, notification)

    }
}