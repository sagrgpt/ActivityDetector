package com.example.activitydetector.mvvm.sensorScreen.repository

interface Repository {
    fun getServiceStatus(): Boolean
    fun getAvailableListOfFiles(): List<String>
    fun setServiceStatus(boolean: Boolean)
    fun setActivityType(activityType: String)
}