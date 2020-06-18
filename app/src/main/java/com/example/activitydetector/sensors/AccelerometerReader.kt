package com.example.activitydetector.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent

class AccelerometerReader(
    context: Context
) : SensorReader(
    context,
    Sensor.TYPE_ACCELEROMETER
) {

    private val alpha: Float = 0.8f
    private var gravity = mutableListOf(0f, 0f, 0f)

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.isAccelerometer()) {
                it.convertToSensorData()
                    .lowPassFilter()
                    .also { data ->
                        publishSubject.onNext(data)
                    }
            }
        }
    }

    private fun Sensor.isAccelerometer(): Boolean {
        return type == sensorType
    }

    private fun SensorData.lowPassFilter(): SensorData {
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z

        return copy(
            x = x - gravity[0],
            y = y - gravity[1],
            z = z - gravity[2]
        )
    }
}


