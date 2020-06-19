package com.example.sensordatagenerator.interfaces

import com.example.sensordatagenerator.model.SensorData
import io.reactivex.rxjava3.core.Observable

interface SensorReader {
    fun start(): Observable<SensorData>
    fun stop()
}