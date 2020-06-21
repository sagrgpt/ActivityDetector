package com.example.activitydetector.di.application

import android.app.Application
import com.example.activitydetector.di.scopes.ApplicationScope
import com.example.activitydetector.utility.StandardScheduler
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun getApplicationContext(): Application {
        return application
    }

    @ApplicationScope
    @Provides
    fun getScheduler(): SchedulerProvider {
        return StandardScheduler()
    }

}
