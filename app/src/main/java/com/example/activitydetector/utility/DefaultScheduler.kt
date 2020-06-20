package com.example.activitydetector.utility

import com.example.sensordatagenerator.interfaces.SchedulerProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


class DefaultScheduler : SchedulerProvider {
    override val io: Scheduler
        get() = Schedulers.io()
    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()!!
    override val computation: Scheduler
        get() = Schedulers.computation()
    override val newThread: Scheduler
        get() = Schedulers.newThread()
}