package com.example.kotlin_demo.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kotlin_demo.R
import com.example.kotlin_demo.activities.TodoAddActivity


//var notificationID = 1
const val channelID = "channel1"

class ReminderBrodcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent){
       /* val a = intent.getStringExtra("title_Extra")
        println("title_Extra")
        println(a)*/

        val extras = intent.extras
        val data = extras!!.getString("title_Extra")
        val mess = extras!!.getString("desc_Extra")
        println("title_Extra")
        println(data)
        println(mess)

        val i = Intent(context,TodoAddActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)


        val builder = NotificationCompat.Builder(context!!, "channel1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(data)
            .setContentText(mess)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)


        val  notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())
    }
}