package com.example.activitydetector.di.application

import android.app.Application
import com.example.activitydetector.mvvm.common.DefaultScheduler
import com.example.sensordatagenerator.interfaces.SchedulerProvider
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
    fun getScheduler(): SchedulerProvider {
        return DefaultScheduler()
    }

}
