package com.example.activitydetector.di.service

import com.example.activitydetector.di.scopes.ServiceScope
import com.example.activitydetector.mvvm.sensorScreen.SensorService
import dagger.Subcomponent

@ServiceScope
@Subcomponent(
    modules = [
        SensorModule::class,
        SensorDataGeneratorModule::class,
        TensorModule::class
    ]
)
interface ServiceComponent {
    fun inject(sensorService: SensorService)
}