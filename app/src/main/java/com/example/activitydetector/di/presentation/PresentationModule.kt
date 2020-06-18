package com.example.activitydetector.di.presentation

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val mActivity: Activity) {

    @Provides
    fun getActivity(): Activity {
        return mActivity
    }

    @Provides
    fun getContext(activity: Activity): Context {
        return activity
    }

}
