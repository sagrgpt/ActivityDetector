package com.example.sensordatagenerator

import com.example.sensordatagenerator.interfaces.FileRetriver
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorListener
import com.example.sensordatagenerator.model.SensorData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataCollector(
    private val accelerometerListener: SensorListener,
    private val gyroscopeListener: SensorListener,
    private val accelerometerRecorder: DataRecorder,
    private val gyroscopeRecorder: DataRecorder,
    private val fileManager: FileRetriver,
    private val writer: DataWriter,
    private val scheduler: SchedulerProvider
) {

    private val time = SimpleDateFormat("MM:dd hh:mm:ss aa", Locale.ROOT)
        .let { dateFormat: DateFormat ->
            val calendar = Calendar.getInstance()
            dateFormat.format(calendar.time)
        }
    private val accelerometerFilename = "${time}_accelerometer.csv"
    private val gyroscopeFilename = "${time}_gyroscope.csv"
    private var activityType: String = "unknown"
    private val disposable: CompositeDisposable = CompositeDisposable()

    fun startCollection(activityType: String) {
        this.activityType = activityType
        startAccelerometerCollection()
        startGyroscopeCollection()
    }

    fun endCollection(): Completable {
        return if (!disposable.isDisposed) {
            disposable.clear()
            writeDataToFile()
        } else Completable.error(
            IllegalStateException("Collector never started")
        )
    }

    private fun startAccelerometerCollection() {
        disposable.add(
            accelerometerListener.listen()
                .subscribeOn(scheduler.computation)
                .observeOn(scheduler.computation)
                .subscribe { observeAccelerometerReadings(it) }
        )

    }

    private fun startGyroscopeCollection() {
        disposable.add(
            gyroscopeListener.listen()
                .subscribeOn(scheduler.computation)
                .observeOn(scheduler.computation)
                .subscribe { observeGyroReadings(it) }
        )
    }

    private fun observeAccelerometerReadings(data: SensorData) {
        accelerometerRecorder.record(
            data.copy(activityType = activityType)
        )
    }

    private fun observeGyroReadings(data: SensorData) {
        gyroscopeRecorder.record(
            data.copy(activityType = activityType)
        )
    }

    private fun writeDataToFile(): Completable {
        return Completable.complete()
            .andThen(writer.writeCsv(
                fileManager.getFile(accelerometerFilename),
                accelerometerRecorder.dataSet))
            .andThen(writer.writeCsv(
                fileManager.getFile(gyroscopeFilename),
                gyroscopeRecorder.dataSet))
    }

}