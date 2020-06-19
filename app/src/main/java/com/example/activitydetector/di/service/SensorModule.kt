package com.example.activitydetector.di.service

import android.app.Application
import com.example.activitydetector.sensors.Accelerometer
import com.example.activitydetector.sensors.Gyroscope
import dagger.Module
import dagger.Provides

@Module
object SensorModule {

    @JvmStatic
    @Provides
    fun getAccelerometerReader(application: Application): Accelerometer {
        return Accelerometer(application)
    }

    @JvmStatic
    @Provides
    fun getGyroscopeReader(application: Application): Gyroscope {
        return Gyroscope(application)
    }
}
