package com.example.activitydetector.mvvm.common

import android.content.Context
import com.example.sensordatagenerator.interfaces.FileRetriver
import java.io.File

class FileManager(
    private val context: Context
) : FileRetriver {

    override fun getFile(filename: String): File {
        return File(createFolder(), filename)
    }

    private fun createFolder(): String {
        val file = File(context.getExternalFilesDir(null), "DataCollector")
        if (!file.exists())
            file.mkdirs()
        return file.path
    }
}