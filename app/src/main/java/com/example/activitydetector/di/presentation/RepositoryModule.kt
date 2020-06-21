package com.example.activitydetector.di.presentation

import com.example.activitydetector.cache.CacheGateway
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.mvvm.sensorScreen.repository.Repository
import com.example.activitydetector.mvvm.sensorScreen.repository.SensorRepository
import com.example.activitydetector.utility.fileManager.FileResourceGateway
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @JvmStatic
    @Provides
    fun provideSensorRepository(
        preferenceConstants: PreferenceConstants,
        sharedPrefManager: CacheGateway,
        fileManager: FileResourceGateway
    ): Repository {
        return SensorRepository(
            preferenceConstants,
            sharedPrefManager,
            fileManager
        )
    }

}