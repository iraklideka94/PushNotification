package com.example.pushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANEL_ID = "chanel_id"
const val CHANEL_NAME = "com.example.pushnotification"

class MyFirebaseMessagingService: FirebaseMessagingService() {




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }




    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, description: String): RemoteViews{
        val remoteView = RemoteViews("com.example.pushnotification",R.layout.notification)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.description,description)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.logo)
        return remoteView
    }

    fun generateNotification(title: String, description: String ){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //chanel id && chanel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            CHANEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,description))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANEL_ID, CHANEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }
}