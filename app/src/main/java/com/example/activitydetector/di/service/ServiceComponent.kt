package com.example.activitydetector.di.service

import com.example.activitydetector.mvvm.SensorService
import dagger.Subcomponent

@Subcomponent(
    modules = [
        SensorModule::class
    ]
)
interface ServiceComponent {
    fun inject(sensorService: SensorService)
}