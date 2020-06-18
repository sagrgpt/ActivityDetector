package com.example.activitydetector.di.application

import android.app.Application
import com.example.activitydetector.cache.SharedPrefManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun getApplicationContext(): Application {
        return application
    }

    @Singleton
    @Provides
    fun getCacheStorage(context: Application): SharedPrefManager {
        return SharedPrefManager(context)
    }

}
