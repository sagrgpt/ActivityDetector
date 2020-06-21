package com.example.activitydetector.di.application

import com.example.activitydetector.di.presentation.PresentationComponent
import com.example.activitydetector.di.presentation.PresentationModule
import com.example.activitydetector.di.scopes.ApplicationScope
import com.example.activitydetector.di.service.ServiceComponent
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        SharedPrefModule::class,
        ViewModelFactoryModule::class
    ]
)
interface AppComponent {
    fun newActivityComponent(presentationModule: PresentationModule): PresentationComponent
    fun newServiceComponent(): ServiceComponent
}