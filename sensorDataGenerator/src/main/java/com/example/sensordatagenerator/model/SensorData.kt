package com.example.sensordatagenerator.model

data class SensorData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val timestamp: Long,
    val accuracy: Accuracy = Accuracy.UNKNOWN,
    val activityType: String = ""
)