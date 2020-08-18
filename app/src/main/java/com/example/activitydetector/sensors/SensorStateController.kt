package com.example.activitydetector.sensors

import com.example.activitydetector.sensors.interfaces.SensorRemote
import com.example.activitydetector.sensors.interfaces.UniversalRemote
import timber.log.Timber

class SensorStateController(
    private val accelerometer: SensorRemote,
    private val gyroscope: SensorRemote
) : UniversalRemote {
    override fun startAccelerometer() {
        try {
            accelerometer.start()
        } catch (e: IllegalStateException) {
            Timber.w("Accelerometer is already running. Avoid repetitive calls.")
        }
    }

    override fun startGyroscope() {
        try {
            gyroscope.start()
        } catch (e: IllegalStateException) {
            Timber.w("Gyroscope is already running. Avoid repetitive calls.")
        }
    }

    override fun stopAccelerometer() {
        try {
            accelerometer.stop()
        } catch (e: IllegalStateException) {
            Timber.e("Accelerometer never started.")
        }
    }

    override fun stopGyroscope() {
        try {
            gyroscope.stop()
        } catch (e: IllegalStateException) {
            Timber.e("Gyroscope never started")
        }
    }

    override fun startAllSensors() {
        startAccelerometer()
        startGyroscope()
    }

    override fun shutdown() {
        stopAccelerometer()
        stopGyroscope()
    }
}