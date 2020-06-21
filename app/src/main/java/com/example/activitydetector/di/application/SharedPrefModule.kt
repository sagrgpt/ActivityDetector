package com.example.activitydetector.di.application

import android.app.Application
import com.example.activitydetector.cache.CacheGateway
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import com.example.activitydetector.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
object SharedPrefModule {

    @JvmStatic
    @ApplicationScope
    @Provides
    fun getCacheStorage(context: Application): CacheGateway {
        return SharedPrefManager(context)
    }

    @JvmStatic
    @ApplicationScope
    @Provides
    fun getPreferenceConstants(): PreferenceConstants {
        return PreferenceConstants()
    }
}