package com.example.activitydetector.mvvm.sensorScreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.activitydetector.mvvm.sensorScreen.model.SensorResults
import com.example.activitydetector.mvvm.sensorScreen.model.SensorResults.*
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEffects
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEffects.*
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEvents
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEvents.*
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewState
import com.example.activitydetector.mvvm.sensorScreen.repository.Repository
import com.example.activitydetector.utility.Lce
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

class SensorViewModel(
    private val repository: Repository
) : ViewModel() {

    private val eventEmitter: PublishSubject<SensorViewEvents> = PublishSubject.create()

    private lateinit var disposable: Disposable

    val viewState: Observable<SensorViewState>
    val viewEffect: Observable<SensorViewEffects>

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun processInput(event: SensorViewEvents) {
        eventEmitter.onNext(event)
    }

    init {
        eventEmitter
            .eventToResult()
            .share()
            .also { result ->
                viewState = result
                    .resultToViewState()
                    .replay(1)
                    .autoConnect(1) { disposable = it }

                viewEffect = result
                    .resultToViewEffect()
            }
    }


    private fun Observable<SensorViewEvents>.eventToResult(): Observable<Lce<out SensorResults>> {
        return publish { o ->
            Observable.mergeArray(
                o.ofType(OnStartViewEvent::class.java).onViewStart(),
                o.ofType(ScreenLoadEvent::class.java).onScreenLoad(),
                o.ofType(StartServiceEvent::class.java).onStartBtnClicked(),
                o.ofType(OnStopViewEvent::class.java).onViewStop(),
                o.ofType(EndServiceEvent::class.java).onEndServiceConnection()
            )
        }
    }

    private fun Observable<Lce<out SensorResults>>.resultToViewState(): Observable<SensorViewState> {
        return scan(SensorViewState()) { vs: SensorViewState, result: Lce<out SensorResults> ->
            when (result) {
                is Lce.Loading -> vs.copy(isProcessing = true)
                is Lce.Content -> result.packet.processResult(vs)
                is Lce.Error -> result.packet.processError(vs)
            }
        }
            .distinctUntilChanged()
    }

    private fun Observable<Lce<out SensorResults>>.resultToViewEffect(): Observable<SensorViewEffects> {
        return filter { it !is Lce.Loading }
            .map<SensorViewEffects> {
                when (it) {
                    is Lce.Content -> it.packet.processEffect()
                    is Lce.Error -> it.packet.processErrorEffect()
                    else -> throw RuntimeException("Fatal call to ViewEffects")
                }
            }.filter { it != NullEffect }
    }

    ///////////////////////////USE-CASES//////////////////////////////
    private fun Observable<OnStartViewEvent>.onViewStart(): Observable<Lce<ServiceBindingResult>> {
        return map {
            Lce.Content(ServiceBindingResult(
                repository.getServiceStatus()
            ))
        }
    }

    private fun Observable<ScreenLoadEvent>.onScreenLoad(): Observable<Lce<ScreenLoadResult>> {
        return map {
            val fileNameList = repository.getAvailableListOfFiles()
            if (fileNameList.isEmpty())
                Lce.Error(ScreenLoadResult())
            else
                Lce.Content(ScreenLoadResult(fileNameList))
        }
    }

    private fun Observable<StartServiceEvent>.onStartBtnClicked(): Observable<Lce<InitiateServiceResult>> {
        return map {
            if (it.isAllowedStorage) {
                repository.setServiceStatus(true)
                repository.setActivityType(it.activityType)
                Lce.Content(InitiateServiceResult)
            } else {
                Lce.Error(InitiateServiceResult)
            }
        }
    }

    private fun Observable<OnStopViewEvent>.onViewStop(): Observable<Lce<ServiceUnbindingResult>> {
        return map {
            if (it.isServiceBounded) {
                Lce.Content(ServiceUnbindingResult(true))
            } else {
                Lce.Content(ServiceUnbindingResult())
            }
        }
    }

    private fun Observable<EndServiceEvent>.onEndServiceConnection(): Observable<Lce<EndServiceResult>> {
        return map {
            if (it.isServiceBounded && it.isAllowedStorage) {
                repository.setServiceStatus(false)
                Lce.Content(EndServiceResult(true))
            } else if (!it.isAllowedStorage) {
                Lce.Error(EndServiceResult(false))
            } else {
                Lce.Content(EndServiceResult(false))
            }
        }
    }

    ///////////////////////////USE-CASES//////////////////////////////

    private fun SensorResults.processResult(vs: SensorViewState) = when (this) {
        is ServiceBindingResult -> vs.copy(isServiceRunning = isServiceRunning)
        is ScreenLoadResult -> vs.copy(nameList = fileList)
        InitiateServiceResult -> vs.copy(isServiceRunning = true)
        is ServiceUnbindingResult -> vs
        is EndServiceResult -> vs.copy(isServiceRunning = !isAllowed)
    }

    private fun SensorResults.processError(vs: SensorViewState) = when (this) {
        is ServiceBindingResult -> vs
        is ScreenLoadResult -> vs.copy(nameList = null)
        InitiateServiceResult -> vs
        is ServiceUnbindingResult -> vs
        is EndServiceResult -> vs
    }

    private fun SensorResults.processEffect(): SensorViewEffects? = when (this) {
        is ServiceBindingResult -> {
            if (isServiceRunning) BindService
            else NullEffect
        }
        is ScreenLoadResult -> NullEffect
        InitiateServiceResult -> StartService
        is ServiceUnbindingResult -> {
            if (isUnbindingAllowed) UnbindService
            else NullEffect
        }
        is EndServiceResult -> {
            if (isAllowed) StopService
            else NullEffect
        }
    }

    private fun SensorResults.processErrorEffect(): SensorViewEffects = when (this) {
        is ServiceBindingResult -> NullEffect
        is ServiceUnbindingResult -> NullEffect
        is ScreenLoadResult -> NullEffect
        is EndServiceResult -> PermissionRequired
        InitiateServiceResult -> PermissionRequired
    }

}



