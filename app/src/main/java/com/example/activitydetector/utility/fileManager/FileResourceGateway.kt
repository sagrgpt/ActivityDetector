package com.example.activitydetector.utility.fileManager

import java.io.File

interface FileResourceGateway {
    fun getFile(filename: String): File
    fun getListOfSharableFiles(): List<String>
    fun getShareable(filename: String): File?
    fun createFolder(): String
}