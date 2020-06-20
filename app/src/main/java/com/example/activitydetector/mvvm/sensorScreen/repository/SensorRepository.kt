package com.example.activitydetector.mvvm.sensorScreen.repository

import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import com.example.activitydetector.utility.FileManager

class SensorRepository(
    private val preferenceConstants: PreferenceConstants,
    private val sharedPrefManager: SharedPrefManager,
    private val fileManager: FileManager
) : Repository {

    override fun getServiceStatus(): Boolean {
        return sharedPrefManager.getBoolean(
            preferenceConstants.isServiceRunning
        )
    }

    override fun getAvailableListOfFiles(): List<String> {
        return fileManager.getListOfSharableFiles()
    }

    override fun setServiceStatus(boolean: Boolean) {
        sharedPrefManager.setBoolean(
            preferenceConstants.isServiceRunning,
            boolean
        )
    }

    override fun setActivityType(activityType: String) {
        sharedPrefManager.setString(
            preferenceConstants.activityType,
            activityType
        )
    }
}