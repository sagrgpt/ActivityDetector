package com.example.activitydetector.di.presentation

import com.example.activitydetector.di.ActivityScope
import com.example.activitydetector.mvvm.sensorScreen.SensorActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        PresentationModule::class,
        ViewModelModule::class,
        RepositoryModule::class
    ])
interface PresentationComponent {
    fun inject(activity: SensorActivity)
}