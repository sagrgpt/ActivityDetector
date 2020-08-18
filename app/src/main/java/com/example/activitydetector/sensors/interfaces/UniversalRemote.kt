package com.example.activitydetector.sensors.interfaces

interface UniversalRemote {

    fun startAccelerometer()
    fun startGyroscope()
    fun stopAccelerometer()
    fun stopGyroscope()
    fun startAllSensors()
    fun shutdown()

}