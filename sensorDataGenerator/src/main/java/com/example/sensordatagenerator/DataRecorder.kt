package com.example.sensordatagenerator

import com.example.sensordatagenerator.model.SensorData

class DataRecorder {
    val dataSet: MutableList<String> = mutableListOf()

    fun record(sensorData: SensorData) {
        dataSet.add("${sensorData.timestamp}|${sensorData.x}|${sensorData.y}|${sensorData.z}|${sensorData.activityType}/")
    }
}