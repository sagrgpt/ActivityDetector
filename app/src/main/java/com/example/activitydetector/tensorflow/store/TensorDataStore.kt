package com.example.activitydetector.tensorflow.store

import com.example.sensordatagenerator.model.SensorData
import com.example.sensordatagenerator.model.SensorType

class TensorDataStore {
    val hashMap = hashMapOf<Long, TensorData>()

    fun record(sensorData: SensorData) {
        when (sensorData.type) {
            SensorType.ACCELEROMETER -> addAccelerometerValues(sensorData)
            SensorType.GYROSCOPE -> addGyroValues(sensorData)
        }
    }

    private fun addAccelerometerValues(data: SensorData) {
        hashMap[data.timestamp] = hashMap[data.timestamp]?.copy(
            x = data.x,
            y = data.y,
            z = data.z
        ) ?: TensorData(
            data.x,
            data.y,
            data.z,
            0.0F,
            0.0F,
            0.0F
        )
    }

    private fun addGyroValues(data: SensorData) {
        hashMap[data.timestamp] = hashMap[data.timestamp]?.copy(
            x2 = data.x,
            y2 = data.y,
            z2 = data.z
        ) ?: TensorData(
            0.0F,
            0.0F,
            0.0F,
            data.x,
            data.y,
            data.z
        )
    }
}