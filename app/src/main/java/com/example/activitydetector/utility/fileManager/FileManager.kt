package com.example.activitydetector.utility.fileManager

import android.content.Context
import com.example.sensordatagenerator.interfaces.FileRetriver
import java.io.File

class FileManager(
    private val context: Context
) : FileRetriver, FileResourceGateway {

    override fun getFile(filename: String): File {
        return File(createFolder(), filename)
    }

    override fun getListOfSharableFiles(): List<String> {
        val nameList = mutableListOf<String>()
        File(
            context.getExternalFilesDir(null),
            "DataCollector"
        )
            .listFiles()
            ?.let { fileList ->
                for (file in fileList) {
                    nameList.add(file.name)
                }
            }
        return nameList
    }

    override fun getShareable(filename: String): File? {
        return File(
            File(context.getExternalFilesDir(null),
                "DataCollector"),
            filename
        )
    }

    override fun createFolder(): String {
        val subdirectory = File(context.getExternalFilesDir(null), "DataCollector")
        if (!subdirectory.exists()) {
            subdirectory.mkdirs()
            subdirectory.setExecutable(true, false)
        }
        return subdirectory.path
    }
}