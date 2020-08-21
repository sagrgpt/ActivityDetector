package com.example.activitydetector.tensorflow.store

import com.example.sensordatagenerator.model.SensorData
import com.example.sensordatagenerator.model.SensorType

class TensorDataStore {
    private var accelerometerList = mutableListOf<SensorData>()
    private var gyroscopeList = mutableListOf<SensorData>()

    fun record(sensorData: SensorData) {
        when (sensorData.type) {
            SensorType.ACCELEROMETER -> accelerometerList.add(sensorData)
            SensorType.GYROSCOPE -> gyroscopeList.add(sensorData)
        }
    }

    fun reset() {
        accelerometerList.clear()
        gyroscopeList.clear()
    }

    fun getTensorDataList(): List<TensorData> {
        val size = accelerometerList.size
            .takeIf { it <= gyroscopeList.size }
            ?: gyroscopeList.size

        return mutableListOf<TensorData>()
            .apply {
                for (i in 0 until size) {
                    add(TensorData(
                        x = accelerometerList[i].x,
                        y = accelerometerList[i].y,
                        z = accelerometerList[i].z,
                        x2 = gyroscopeList[i].x,
                        y2 = gyroscopeList[i].y,
                        z2 = gyroscopeList[i].z
                    ))
                }
            }

    }
}