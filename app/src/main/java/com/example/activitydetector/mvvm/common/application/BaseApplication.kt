package com.example.activitydetector.mvvm.common.application

import android.app.Application
import com.example.activitydetector.BuildConfig
import com.example.activitydetector.di.application.AppComponent
import com.example.activitydetector.di.application.ApplicationModule
import com.example.activitydetector.di.application.DaggerAppComponent
import com.example.activitydetector.timber.CrashlyticsTree
import timber.log.Timber

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        Timber.plant(CrashlyticsTree())
    }

}