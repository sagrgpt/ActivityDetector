package com.example.activitydetector.di.service

import com.example.activitydetector.mvvm.SensorService
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ServiceModule::class
    ]
)
interface ServiceComponent {
    fun inject(sensorService: SensorService)
}