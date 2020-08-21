package com.example.activitydetector.tensorflow

import com.example.activitydetector.tensorflow.store.SensorStore
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ActivityDetector(
    private val client: CnnClient,
    private val store: SensorStore,
    private val scheduler: SchedulerProvider
) {

    private val minRequiredSizeForPrediction = 200
    private var monitorDisposable: Disposable? = null

    fun monitor() {
        Timber.v("Initiating activity detector")
        client.load()
        store.startStorage()
        monitorDisposable = Observable.interval(
            5,
            10,
            TimeUnit.SECONDS
        )
            .subscribeOn(scheduler.computation)
            .subscribe { triggerPrediction() }
    }

    private fun triggerPrediction() {
        Timber.v("Prediction triggered. Getting data list")
        store.getDataList()
            .also { Timber.v("Current size of data list is: ${it.size}") }
            .takeIf { it.size >= minRequiredSizeForPrediction }
            ?.let { client.predict(it) }
            ?.also {
                Timber.d("Detected Activity of user is $it")
                store.resetStore()
            }
            ?: Timber.w("Size of data list is below $minRequiredSizeForPrediction")
    }

    fun shutdown() {
        store.stopStorage()
        monitorDisposable?.dispose()
    }

}