package com.example.activitydetector.di.presentation

import androidx.lifecycle.ViewModel
import com.example.activitydetector.di.ViewModelKey
import com.example.activitydetector.mvvm.sensorScreen.repository.Repository
import com.example.activitydetector.mvvm.sensorScreen.viewmodel.SensorViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(SensorViewModel::class)
    fun providesSensorViewModel(repository: Repository): ViewModel {
        return SensorViewModel(repository)
    }

}
