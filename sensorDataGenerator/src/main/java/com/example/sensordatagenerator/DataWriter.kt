package com.example.sensordatagenerator

import com.example.filewriter.CsvWriter
import com.example.filewriter.FileCallback
import io.reactivex.rxjava3.core.Completable
import java.io.File

class DataWriter(
    private val csvWriter: CsvWriter
) {

    private val header: List<String> = listOf("Timestamp|X|Y|Z/")

    fun writeCsv(
        file: File,
        dataSet: List<String>
    ): Completable {
        return Completable.create { emitter ->
            csvWriter.setSeparatorColumn("|")
            csvWriter.setSeparatorLine("/")

            csvWriter.createCsvFile(
                file,
                header,
                dataSet,
                object : FileCallback {
                    override fun onSuccess(file: File?) {
                        emitter.onComplete()
                    }

                    override fun onFail(err: String?) {
                        emitter.onError(RuntimeException(err))
                        emitter.onComplete()
                    }
                }
            )
        }
    }
}