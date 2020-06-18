package com.example.activitydetector.sensors

data class SensorData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val timestamp: Long,
    val accuracy: Accuracy = Accuracy.UNKNOWN
)

enum class Accuracy {
    UNRELIABLE,
    HIGH,
    LOW,
    MEDIUM,
    UNKNOWN
}