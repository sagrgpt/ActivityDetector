package com.example.activitydetector.di.presentation

import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import com.example.activitydetector.mvvm.sensorScreen.repository.Repository
import com.example.activitydetector.mvvm.sensorScreen.repository.SensorRepository
import com.example.activitydetector.utility.FileManager
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {

    @JvmStatic
    @Provides
    fun provideSensorRepository(
        preferenceConstants: PreferenceConstants,
        sharedPrefManager: SharedPrefManager,
        fileManager: FileManager
    ): Repository {
        return SensorRepository(
            preferenceConstants,
            sharedPrefManager,
            fileManager
        )
    }

}