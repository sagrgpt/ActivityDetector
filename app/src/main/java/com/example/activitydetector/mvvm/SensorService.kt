package com.example.activitydetector.mvvm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.activitydetector.mvvm.common.service.BaseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SensorService : BaseService() {

    private val foregroundId = 220

    private val binder = MyBinder()

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        getComponent().inject(this)
        Log.v("SensorService", "Service created")
        startForeground(foregroundId, buildNotification())
        doSomething()
    }

    private fun doSomething() {
        disposable = Observable.interval(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe(
                { Log.d("SensorService", "Service is working") },
                { Log.e("SensorService", "Some error occurred") }
            )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SensorService", "Service is destroyed")
    }


    fun stop() {
        disposable?.dispose()
        stopSelf()
    }

    private fun buildNotification(): Notification {
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
            val mChannel = NotificationChannel("101", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        return "101"
    }

    inner class MyBinder : Binder() {
        val service: SensorService
            get() = this@SensorService
    }
}