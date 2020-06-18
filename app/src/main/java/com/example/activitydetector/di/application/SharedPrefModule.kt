package com.example.activitydetector.di.application

import android.app.Application
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SharedPrefModule {

    @JvmStatic
    @Singleton
    @Provides
    fun getCacheStorage(context: Application): SharedPrefManager {
        return SharedPrefManager(context)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun getPreferenceConstants(): PreferenceConstants {
        return PreferenceConstants()
    }
}