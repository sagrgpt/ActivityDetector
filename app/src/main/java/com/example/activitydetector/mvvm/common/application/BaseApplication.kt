package com.example.activitydetector.mvvm.common.application

import android.app.Application
import com.example.activitydetector.di.application.AppComponent
import com.example.activitydetector.di.application.ApplicationModule
import com.example.activitydetector.di.application.DaggerAppComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

}