package com.example.activitydetector.di.presentation

import android.app.Activity
import android.content.Context
import com.example.activitydetector.di.scopes.ActivityScope
import com.example.activitydetector.mvvm.adapter.FileListAdapter
import com.example.activitydetector.utility.PermissionUtility
import com.example.activitydetector.utility.ShareUtility
import com.example.activitydetector.utility.fileManager.FileManager
import com.example.activitydetector.utility.fileManager.FileResourceGateway
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.subjects.PublishSubject

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

    @Provides
    fun getPermissionUtility(): PermissionUtility {
        return PermissionUtility()
    }

    @Provides
    fun getFileManger(application: Context): FileResourceGateway {
        return FileManager(application)
    }

    @Provides
    fun getShareUtility(application: Context): ShareUtility {
        return ShareUtility(application)
    }

    @ActivityScope
    @Provides
    fun getItemClickListener(): PublishSubject<String> {
        return PublishSubject.create()
    }

    @ActivityScope
    @Provides
    fun getFileListAdapter(clickListener: PublishSubject<String>): FileListAdapter {
        return FileListAdapter { clickListener.onNext(it) }
    }
}
