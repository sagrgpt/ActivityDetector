package com.example.activitydetector.mvvm.common.activity

import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.activitydetector.di.application.AppComponent
import com.example.activitydetector.di.presentation.PresentationComponent
import com.example.activitydetector.di.presentation.PresentationModule
import com.example.activitydetector.mvvm.common.application.BaseApplication

open class BaseActivity : AppCompatActivity() {

    var isInjectorUsed = false

    @UiThread
    fun getComponent(): PresentationComponent {
        if (isInjectorUsed)
            throw RuntimeException("No need to inject dependencies again")
        isInjectorUsed = true
        return getAppComponent()
            .newActivityComponent(PresentationModule(this))
    }

    private fun getAppComponent(): AppComponent {
        return (application as BaseApplication).appComponent
    }

}