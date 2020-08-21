package com.example.activitydetector.di.service

import android.app.Application
import com.example.activitydetector.sensors.Accelerometer
import com.example.activitydetector.sensors.Gyroscope
import com.example.activitydetector.tensorflow.ActivityDetector
import com.example.activitydetector.tensorflow.CnnClient
import com.example.activitydetector.tensorflow.store.SensorStore
import com.example.activitydetector.tensorflow.store.TensorDataStore
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
object TensorModule {

    @JvmStatic
    @Provides
    fun getCnnClient(application: Application): CnnClient {
        return CnnClient(application)
    }

    @JvmStatic
    @Provides
    fun getTensorDataStore(): TensorDataStore {
        return TensorDataStore()
    }

    @JvmStatic
    @Provides
    fun getSensorStore(
        accelerometer: Accelerometer,
        gyroscope: Gyroscope,
        tensorDataStore: TensorDataStore,
        schedulerProvider: SchedulerProvider
    ): SensorStore {
        return SensorStore(
            accelerometer,
            gyroscope,
            tensorDataStore,
            schedulerProvider
        )
    }

    @JvmStatic
    @Provides
    fun getActivityDetector(
        client: CnnClient,
        store: SensorStore,
        schedulerProvider: SchedulerProvider
    ): ActivityDetector {
        return ActivityDetector(
            client,
            store,
            schedulerProvider
        )
    }

}