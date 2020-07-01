package com.home.traker.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.home.traker.R


class ForegroundService : Service() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, ForegroundService::class.java)
            )
        }

        @JvmStatic
        fun stop(context: Context) {
            context.stopService(Intent(context, ForegroundService::class.java))
        }
    }

    // Foreground service notification =========

    private val foregroundNotificationId: Int = (System.currentTimeMillis() % 10000).toInt()
    private val foregroundNotification by lazy {

        NotificationCompat.Builder(this, foregroundNotificationChannelId)
            .setSmallIcon(R.drawable.traker_logo)
            .setContentTitle("My notification")
            .setContentText("User Punched-IN. Sharing location...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("User Punched-IN. Sharing location...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(null)
            .build()
    }
    private val foregroundNotificationChannelName by lazy {
        getString(R.string.app_name)
    }
    private val foregroundNotificationChannelDescription by lazy {
        "User Punched-IN. Sharing location"
    }
    private val foregroundNotificationChannelId by lazy {
        "ForegroundServiceSample.NotificationChannel".also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                    if (getNotificationChannel(it) == null) {
                        createNotificationChannel(
                            NotificationChannel(
                                it,
                                foregroundNotificationChannelName,
                                NotificationManager.IMPORTANCE_MIN
                            ).also {
                                it.description = foregroundNotificationChannelDescription
                                it.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
                                it.vibrationPattern = null
                                it.setSound(null, null)
                                it.setShowBadge(false)
                            })
                    }
                }
            }
        }
    }


    // Lifecycle ===============================

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(foregroundNotificationId, foregroundNotification)


        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}