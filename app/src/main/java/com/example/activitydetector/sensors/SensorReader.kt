package com.example.activitydetector.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

abstract class SensorReader(
    context: Context,
    val sensorType: Int
) : SensorEventListener {

    private val sensorManager = (context.getSystemService(Context.SENSOR_SERVICE)
        as SensorManager)

    private val mSensor: Sensor = sensorManager.getDefaultSensor(sensorType)

    private var sensorAccuracy = Accuracy.LOW

    internal var publishSubject: PublishSubject<SensorData> = PublishSubject.create()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        sensor?.run {
            if (type == sensorType) {
                sensorAccuracy = getAccuracy(accuracy)
            }
        }
    }


    fun start(): Observable<SensorData> {
        sensorManager.registerListener(
            this,
            mSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
        return publishSubject
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        Timber.v("Listener Unregistered")
        publishSubject.onComplete()
    }

    internal fun SensorEvent.convertToSensorData(): SensorData {
        return SensorData(
            x = values[0],
            y = values[1],
            z = values[2],
            timestamp = timestamp,
            accuracy = sensorAccuracy
        )
    }

    private fun getAccuracy(accuracy: Int): Accuracy = when (accuracy) {
        SensorManager.SENSOR_STATUS_UNRELIABLE -> Accuracy.UNRELIABLE
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> Accuracy.HIGH
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> Accuracy.LOW
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> Accuracy.MEDIUM
        else -> Accuracy.UNKNOWN
    }
}