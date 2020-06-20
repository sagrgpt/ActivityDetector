package com.example.activitydetector.di.service

import android.app.Application
import com.example.activitydetector.sensors.Accelerometer
import com.example.activitydetector.sensors.Gyroscope
import com.example.activitydetector.utility.FileManager
import com.example.sensordatagenerator.DataCollector
import com.example.sensordatagenerator.factory.GeneratorFactory
import com.example.sensordatagenerator.interfaces.FileRetriver
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
object SensorDataGeneratorModule {

    @JvmStatic
    @Provides
    fun getFileRetriver(context: Application): FileRetriver {
        return FileManager(context)
    }

    @JvmStatic
    @Provides
    fun getGeneratorFactory(
        accelerometer: Accelerometer,
        gyroscope: Gyroscope,
        fileManager: FileRetriver,
        scheduler: SchedulerProvider
    ): GeneratorFactory {
        return GeneratorFactory(
            accelerometer,
            gyroscope,
            fileManager,
            scheduler
        )
    }

    @JvmStatic
    @Provides
    fun getDataCollector(factory: GeneratorFactory): DataCollector {
        return factory.buildDataCollector()
    }
}
