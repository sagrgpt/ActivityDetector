package com.example.activitydetector.di.presentation

import com.example.activitydetector.mvvm.SensorActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [
        PresentationModule::class/*,
        ViewModelModule::class*/
    ])
interface PresentationComponent {
    fun inject(activity: SensorActivity)
}