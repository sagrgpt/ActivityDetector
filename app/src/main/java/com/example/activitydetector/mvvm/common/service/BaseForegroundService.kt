package com.example.activitydetector.mvvm.common.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.annotation.UiThread
import androidx.core.app.NotificationCompat
import com.example.activitydetector.di.application.AppComponent
import com.example.activitydetector.di.service.ServiceComponent
import com.example.activitydetector.mvvm.common.application.BaseApplication
import timber.log.Timber

abstract class BaseForegroundService : Service() {

    private val foregroundId = 220
    private var isInjectorUsed = false
    private val channelId = "101"

    override fun onCreate() {
        super.onCreate()
        startForeground(foregroundId, buildNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("Service is destroyed")
    }

    @UiThread
    fun getComponent(): ServiceComponent {
        if (isInjectorUsed)
            throw RuntimeException("No need to inject dependencies again")
        isInjectorUsed = true
        return getAppComponent()
            .newServiceComponent()
    }

    private fun getAppComponent(): AppComponent {
        return (application as BaseApplication).appComponent
    }

    internal fun buildNotification(): Notification {
        val builder = NotificationCompat.Builder(this, getChannelId())
        builder.setOngoing(true)
            .setContentTitle("Data Collector is running")
        return builder.build()
    }

    private fun getChannelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "ActivityDetector"
            val descriptionText = "General Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        return channelId
    }


}