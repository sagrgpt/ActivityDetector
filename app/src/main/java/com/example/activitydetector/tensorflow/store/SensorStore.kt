package com.example.activitydetector.tensorflow.store

import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorListener
import com.example.sensordatagenerator.model.SensorData

class SensorStore(
    private val accelerometerListener: SensorListener,
    private val gyroscopeListener: SensorListener,
    private val dataStore: TensorDataStore,
    private val scheduler: SchedulerProvider
) {

    fun startMonitoring() {
        accelerometerListener.listen()
            .subscribeOn(scheduler.computation)
            .observeOn(scheduler.computation)
            .subscribe { observeReadings(it) }
        gyroscopeListener.listen()
            .subscribeOn(scheduler.computation)
            .observeOn(scheduler.computation)
            .subscribe { observeReadings(it) }

    }

    fun getDataList(): List<TensorData> {
        return dataStore.hashMap
            .values
            .toList()
    }

    private fun observeReadings(data: SensorData) {
        dataStore.record(data)
    }

}