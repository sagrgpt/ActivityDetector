package com.example.activitydetector.mvvm.sensorScreen.model

data class SensorViewState(
    val isProcessing: Boolean = true,
    val isServiceRunning: Boolean = false,
    val nameList: List<String>? = null
)

sealed class SensorViewEvents {
    object ScreenLoadEvent : SensorViewEvents()
    object OnStartViewEvent : SensorViewEvents()

    data class StartServiceEvent(
        val isAllowedStorage: Boolean,
        val activityType: String
    ) : SensorViewEvents()

    data class OnStopViewEvent(
        val isServiceBounded: Boolean
    ) : SensorViewEvents()

    data class EndServiceEvent(
        val isServiceBounded: Boolean,
        val isAllowedStorage: Boolean
    ) : SensorViewEvents()
}

sealed class SensorViewEffects {
    object BindService : SensorViewEffects()
    object UnbindService : SensorViewEffects()
    object StartService : SensorViewEffects()
    object StopService : SensorViewEffects()
    object PermissionRequired : SensorViewEffects()
    object NullEffect : SensorViewEffects()
}

sealed class SensorResults {
    data class ServiceBindingResult(
        val isServiceRunning: Boolean = false
    ) : SensorResults()

    data class ServiceUnbindingResult(
        val isUnbindingAllowed: Boolean = false
    ) : SensorResults()

    data class ScreenLoadResult(
        val fileList: List<String> = emptyList()
    ) : SensorResults()

    data class EndServiceResult(
        val isAllowed: Boolean
    ) : SensorResults()

    object InitiateServiceResult : SensorResults()
}