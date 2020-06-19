package com.example.sensordatagenerator.interfaces

import java.io.File

interface FileRetriver {
    fun getFile(filename: String): File
}