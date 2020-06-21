package com.example.activitydetector.mvvm.sensorScreen.repository

import com.example.activitydetector.cache.CacheGateway
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.utility.fileManager.FileResourceGateway

class SensorRepository(
    private val preferenceConstants: PreferenceConstants,
    private val sharedPrefManager: CacheGateway,
    private val fileManager: FileResourceGateway
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