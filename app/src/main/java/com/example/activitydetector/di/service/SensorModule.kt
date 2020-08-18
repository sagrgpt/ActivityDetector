package com.example.activitydetector.di.service

import android.app.Application
import com.example.activitydetector.sensors.Accelerometer
import com.example.activitydetector.sensors.Gyroscope
import com.example.activitydetector.sensors.SensorStateController
import com.example.activitydetector.sensors.interfaces.UniversalRemote
import dagger.Module
import dagger.Provides

@Module
object SensorModule {

    @JvmStatic
    @Provides
    fun getAccelerometer(application: Application): Accelerometer {
        return Accelerometer(application)
    }

    @JvmStatic
    @Provides
    fun getGyroscope(application: Application): Gyroscope {
        return Gyroscope(application)
    }

    @JvmStatic
    @Provides
    fun getUniversalRemote(accelerometer: Accelerometer, gyroscope: Gyroscope): UniversalRemote {
        return SensorStateController(
            accelerometer,
            gyroscope
        )
    }
}
