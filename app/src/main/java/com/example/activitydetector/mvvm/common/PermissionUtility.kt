package com.example.activitydetector.mvvm.common

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class PermissionUtility {

    /**
     * Request Runtime permission to user
     * @param activity Referenced activity
     * @param permission Permission Name
     * @param requestCode Requested permission's code
     * @return
     */
    fun askPermissionForActivity(activity: Activity, permission: String, requestCode: Int): Boolean {
        val permissionList: MutableList<String> = ArrayList()
        val permissionId = ContextCompat.checkSelfPermission(activity.applicationContext, permission)
        if (permissionId != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission)
            ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), requestCode)
        } else {
            return true
        }
        return false
    }
}