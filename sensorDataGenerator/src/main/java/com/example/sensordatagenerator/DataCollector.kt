package com.example.sensordatagenerator

import com.example.sensordatagenerator.interfaces.FileRetriver
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorReader
import com.example.sensordatagenerator.model.SensorData
import io.reactivex.rxjava3.core.Completable
import java.util.*

class DataCollector(
    private val accelerometerReader: SensorReader,
    private val gyroscopeReader: SensorReader,
    private val accelerometerRecorder: DataRecorder,
    private val gyroscopeRecorder: DataRecorder,
    private val fileManager: FileRetriver,
    private val writer: DataWriter,
    private val scheduler: SchedulerProvider
) {

    private val startTime = Calendar.getInstance().timeInMillis
    private val accelerometerFilename = "accelerometer_$startTime.csv"
    private val gyroscopeFilename = "gyroscope_$startTime.csv"

    fun startCollection() {
        startAccelerometerCollection()
        startGyroscopeCollection()
    }

    fun endCollection(): Completable {
        accelerometerReader.stop()
        gyroscopeReader.stop()
        return Completable.complete()
            .andThen(writer.writeCsv(
                fileManager.getFile(accelerometerFilename),
                accelerometerRecorder.dataSet))
            .andThen(writer.writeCsv(
                fileManager.getFile(gyroscopeFilename),
                gyroscopeRecorder.dataSet))
    }

    private fun startAccelerometerCollection() {
        accelerometerReader.start()
            .subscribeOn(scheduler.computation)
            .observeOn(scheduler.computation)
            .subscribe { observeAccelerometerReadings(it) }

    }

    private fun startGyroscopeCollection() {
        gyroscopeReader.start()
            .subscribeOn(scheduler.computation)
            .observeOn(scheduler.computation)
            .subscribe { observeGyroReadings(it) }
    }

    private fun observeAccelerometerReadings(data: SensorData) {
        accelerometerRecorder.record(data)
    }

    private fun observeGyroReadings(data: SensorData) {
        gyroscopeRecorder.record(data)
    }
}