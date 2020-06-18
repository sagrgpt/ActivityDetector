package com.example.activitydetector.mvvm

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.activitydetector.mvvm.common.service.BaseForegroundService
import com.example.activitydetector.schedulers.SchedulerProvider
import com.example.activitydetector.sensors.AccelerometerReader
import com.example.activitydetector.sensors.SensorData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SensorService : BaseForegroundService() {

    private val binder = MyBinder()
    private var compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var accelerometerReader: AccelerometerReader

    @Inject
    lateinit var scheduler: SchedulerProvider

    override fun onCreate() {
        super.onCreate()
        getComponent().inject(this)
        Timber.v("Service created")
        initiateAccelerometer()
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun stop() {
        accelerometerReader.stop()
        stopSelf()
    }

    private fun initiateAccelerometer() {
        compositeDisposable.add(
            accelerometerReader.start()
                .subscribeOn(scheduler.computation)
                .observeOn(scheduler.computation)
                .subscribe(
                    { observeAccelerometer(it) },
                    { Timber.e(it, "Error reading accelerometer") },
                    { Timber.v("Stopped reading accelerometer") }
                )
        )

    }

    private fun observeAccelerometer(data: SensorData) {
        Timber.d("${data.accuracy} accuracy data: ${data.x}|${data.y}|${data.z}/")
    }

    inner class MyBinder : Binder() {
        val service: SensorService
            get() = this@SensorService
    }
}