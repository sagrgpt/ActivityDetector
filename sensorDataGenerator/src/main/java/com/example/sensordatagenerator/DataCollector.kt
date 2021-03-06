package com.example.sensordatagenerator

import com.example.sensordatagenerator.interfaces.FileRetriver
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorReader
import com.example.sensordatagenerator.model.SensorData
import io.reactivex.rxjava3.core.Completable
import java.text.DateFormat
import java.text.SimpleDateFormat
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

    private val time = SimpleDateFormat("MM:dd hh:mm:ss aa", Locale.ROOT)
        .let { dateFormat: DateFormat ->
            val calendar = Calendar.getInstance()
            dateFormat.format(calendar.time)
        }
    private val accelerometerFilename = "${time}_accelerometer.csv"
    private val gyroscopeFilename = "${time}_gyroscope.csv"
    private var activityType: String = "unknown"

    fun startCollection(activityType: String) {
        this.activityType = activityType
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
        accelerometerRecorder.record(
            data.copy(activityType = activityType)
        )
    }

    private fun observeGyroReadings(data: SensorData) {
        gyroscopeRecorder.record(
            data.copy(activityType = activityType)
        )
    }
}