package com.example.activitydetector.mvvm.common.service

import android.app.Service
import androidx.annotation.UiThread
import com.example.activitydetector.di.application.AppComponent
import com.example.activitydetector.di.service.ServiceComponent
import com.example.activitydetector.mvvm.common.application.BaseApplication

abstract class BaseService : Service() {

    var isInjectorUsed = false

    @UiThread
    fun getComponent(): ServiceComponent {
        if (isInjectorUsed)
            throw RuntimeException("No need to inject dependencies again")
        isInjectorUsed = true
        return getAppComponent()
            .newServiceComponent()
    }

    private fun getAppComponent(): AppComponent {
        return (application as BaseApplication).appComponent
    }

}