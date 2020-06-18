package com.example.activitydetector.di.service

import android.app.Application
import com.example.activitydetector.sensors.AccelerometerReader
import com.example.activitydetector.sensors.GyroscopeReader
import dagger.Module
import dagger.Provides

@Module
object SensorModule {

    @JvmStatic
    @Provides
    fun getAccelerometerReader(application: Application): AccelerometerReader {
        return AccelerometerReader(application)
    }

    @JvmStatic
    @Provides
    fun getGyroscopeReader(application: Application): GyroscopeReader {
        return GyroscopeReader(application)
    }
}
