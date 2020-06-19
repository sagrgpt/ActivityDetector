package com.example.sensordatagenerator.interfaces

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    val io: Scheduler
    val ui: Scheduler
    val computation: Scheduler
    val newThread: Scheduler
}