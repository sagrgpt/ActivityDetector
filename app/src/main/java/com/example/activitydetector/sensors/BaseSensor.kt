package com.example.activitydetector.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.activitydetector.sensors.interfaces.SensorRemote
import com.example.sensordatagenerator.interfaces.SensorListener
import com.example.sensordatagenerator.model.Accuracy
import com.example.sensordatagenerator.model.SensorData
import com.example.sensordatagenerator.model.SensorType
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

abstract class BaseSensor(
    context: Context,
    val sensorType: Int
) : SensorRemote, SensorListener, SensorEventListener {

    private val sensorManager = (context.getSystemService(Context.SENSOR_SERVICE)
        as SensorManager)

    private var isSensorRunning: Boolean = false

    private val mSensor: Sensor? = sensorManager.getDefaultSensor(sensorType)

    private var sensorAccuracy = Accuracy.LOW

    internal var publishSubject: PublishSubject<SensorData> = PublishSubject.create()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        sensor?.run {
            if (type == sensorType) {
                sensorAccuracy = getAccuracy(accuracy)
            }
        }
    }

    override fun start() {
        if (isSensorRunning)
            throw IllegalStateException("Sensor has already started")
        else
            registerSensor()
    }

    override fun stop() {
        if (isSensorRunning) {
            unregisterSensor()
            publishSubject.onComplete()
        } else throw IllegalStateException("Sensor never started")
    }

    override fun listen(): Observable<SensorData> {
        return publishSubject
    }

    internal fun SensorEvent.convertToSensorData(
        sensorType: SensorType
    ): SensorData {
        return SensorData(
            x = values[0],
            y = values[1],
            z = values[2],
            timestamp = timestamp,
            accuracy = sensorAccuracy,
            type = sensorType
        )
    }

    private fun registerSensor() {
        sensorManager.registerListener(
            this,
            mSensor,
            SensorManager.SENSOR_DELAY_GAME
        )
        isSensorRunning = true
    }

    private fun unregisterSensor() {
        sensorManager.unregisterListener(this)
        Timber.v("Listener Unregistered")
        isSensorRunning = false
    }

    private fun getAccuracy(accuracy: Int): Accuracy = when (accuracy) {
        SensorManager.SENSOR_STATUS_UNRELIABLE -> Accuracy.UNRELIABLE
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> Accuracy.HIGH
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> Accuracy.LOW
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> Accuracy.MEDIUM
        else -> Accuracy.UNKNOWN
    }
}
