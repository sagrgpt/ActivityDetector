package com.example.sensordatagenerator.factory

import com.example.filewriter.CsvWriter
import com.example.sensordatagenerator.DataCollector
import com.example.sensordatagenerator.DataRecorder
import com.example.sensordatagenerator.DataWriter
import com.example.sensordatagenerator.interfaces.FileRetriver
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import com.example.sensordatagenerator.interfaces.SensorListener

class GeneratorFactory(
    private val accelerometerListener: SensorListener,
    private val gyroscopeListener: SensorListener,
    private val fileManager: FileRetriver,
    private val scheduler: SchedulerProvider) {

    fun buildDataCollector(): DataCollector {
        return DataCollector(
            accelerometerListener,
            gyroscopeListener,
            buildDataRecorder(),
            buildDataRecorder(),
            fileManager,
            buildDataWriter(),
            scheduler
        )
    }

    private fun buildDataRecorder(): DataRecorder {
        return DataRecorder()
    }

    private fun buildDataWriter(): DataWriter {
        return DataWriter(buildCsvWriter())
    }

    private fun buildCsvWriter(): CsvWriter {
        return CsvWriter()
    }

}