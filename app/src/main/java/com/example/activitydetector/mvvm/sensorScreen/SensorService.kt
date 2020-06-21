package com.example.activitydetector.mvvm.sensorScreen

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.activitydetector.cache.CacheGateway
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.mvvm.common.service.BaseForegroundService
import com.example.sensordatagenerator.DataCollector
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import io.reactivex.rxjava3.core.Completable
import timber.log.Timber
import javax.inject.Inject

class SensorService : BaseForegroundService() {

    private val binder = MyBinder()

    @Inject
    lateinit var dataCollector: DataCollector

    @Inject
    lateinit var scheduler: SchedulerProvider

    @Inject
    lateinit var sharedPrefManager: CacheGateway

    @Inject
    lateinit var preferenceConstants: PreferenceConstants

    override fun onCreate() {
        super.onCreate()
        getComponent().inject(this)
        Timber.v("Service created")
        dataCollector.startCollection(
            sharedPrefManager.getString(preferenceConstants.activityType)
        )
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    fun closeResources(): Completable {
        return dataCollector.endCollection()
            .subscribeOn(scheduler.computation)
    }

    inner class MyBinder : Binder() {
        val service: SensorService
            get() = this@SensorService
    }
}