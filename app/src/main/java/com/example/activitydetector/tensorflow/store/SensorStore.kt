package com.example.activitydetector.tensorflow.store

import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorListener
import com.example.sensordatagenerator.model.SensorData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SensorStore(
    private val accelerometerListener: SensorListener,
    private val gyroscopeListener: SensorListener,
    private val dataStore: TensorDataStore,
    private val scheduler: SchedulerProvider
) {

    private val disposable = CompositeDisposable()

    fun startStorage() {
        listenToAccelerometer()
        listenToGyroscope()
    }

    fun getDataList(): List<TensorData> {
        return dataStore.getTensorDataList()
    }

    fun resetStore() {
        dataStore.reset()
    }

    fun stopStorage() {
        disposable.clear()
        dataStore.reset()
    }

    private fun listenToAccelerometer() {
        disposable.add(
            accelerometerListener.listen()
                .subscribeOn(scheduler.computation)
                .observeOn(scheduler.computation)
                .subscribe { observeReadings(it) }
        )
    }

    private fun listenToGyroscope() {
        disposable.add(
            gyroscopeListener.listen()
                .subscribeOn(scheduler.computation)
                .observeOn(scheduler.computation)
                .subscribe { observeReadings(it) }
        )
    }

    private fun observeReadings(data: SensorData) {
        dataStore.record(data)
    }

}