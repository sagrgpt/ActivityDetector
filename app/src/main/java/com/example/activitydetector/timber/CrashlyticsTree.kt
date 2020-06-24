package com.example.activitydetector.timber

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    private val CRASHLYTICS_KEY_PRIORITY = "priority"
    private val CRASHLYTICS_KEY_TAG = "tag"
    private val CRASHLYTICS_KEY_MESSAGE = "message"


    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        if (priority == Log.WARN) {
            t?.let { FirebaseCrashlytics.getInstance().recordException(it) }
                ?: FirebaseCrashlytics.getInstance().recordException(Exception(message))
        } else {
            t?.let { FirebaseCrashlytics.getInstance().log("$message: ${it.message}") }
                ?: FirebaseCrashlytics.getInstance().log(message)
        }

    }
}