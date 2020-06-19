package com.example.activitydetector.sensors

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent

class Gyroscope(
    application: Application
) : BaseSensor(
    application,
    Sensor.TYPE_GYROSCOPE
) {

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.isGyroscope()) {
                it.convertToSensorData()
                    .also { data ->
                        publishSubject.onNext(data)
                    }
            }
        }
    }

    private fun Sensor.isGyroscope(): Boolean {
        return type == sensorType
    }

}