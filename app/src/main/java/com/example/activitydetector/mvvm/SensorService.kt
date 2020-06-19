package com.example.activitydetector.mvvm

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.activitydetector.mvvm.common.service.BaseForegroundService
import com.example.sensordatagenerator.DataCollector
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class SensorService : BaseForegroundService() {

    private val binder = MyBinder()

    @Inject
    lateinit var dataCollector: DataCollector

    @Inject
    lateinit var scheduler: SchedulerProvider

    override fun onCreate() {
        super.onCreate()
        getComponent().inject(this)
        Timber.v("Service created")
        dataCollector.startCollection()
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    fun stop() {
        dataCollector.endCollection()
            .subscribeOn(scheduler.computation)
            .observeOn(scheduler.io)
            .subscribe(
                {
                    Timber.v("Files successfully saved")
                    stopSelf()
                },
                { Timber.e(it, "Unable to save file") }
            )
    }

    inner class MyBinder : Binder() {
        val service: SensorService
            get() = this@SensorService
    }
}