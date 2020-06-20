package com.example.activitydetector.mvvm


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.activitydetector.mvvm.sensorScreen.SensorViewEffects
import com.example.activitydetector.mvvm.sensorScreen.SensorViewEvents
import com.example.activitydetector.mvvm.sensorScreen.repository.Repository
import com.example.activitydetector.mvvm.sensorScreen.viewmodel.SensorViewModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SensorViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repo: Repository

    private lateinit var viewmodel: SensorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun serviceNotStarted_onStartActivity_noEffect() {
        viewmodel = SensorViewModel(
            repo
        )
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        `when`(repo.getServiceStatus())
            .thenReturn(false)

        viewmodel.processInput(SensorViewEvents.OnStartViewEvent)

        viewStateTester.assertValueCount(1)
        viewEffectTester.assertValueCount(0)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
    }

    @Test
    fun serviceStarted_onStartActivity_StartConnection() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        `when`(repo.getServiceStatus())
            .thenReturn(true)

        viewmodel.processInput(SensorViewEvents.OnStartViewEvent)

        viewStateTester.assertValueAt(1) {
            assertThat(!it.isProcessing)
            assertThat(it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueAt(0) {
            assertThat(it is SensorViewEffects.BindService)
            true
        }
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
    }

    @Test
    fun startBtnClicked_initiateService() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()


        viewmodel.processInput(SensorViewEvents.StartServiceEvent(
            true,
            "Running"
        ))

        viewStateTester.assertValueAt(1) {
            assertThat(!it.isProcessing)
            assertThat(it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueAt(0) {
            assertThat(it is SensorViewEffects.StartService)
            true
        }
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, times(1)).setServiceStatus(true)
        verify(repo, times(1)).setActivityType("Running")
    }

    @Test
    fun serviceRunning_onStopEvent_unbindService() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()
        `when`(repo.getServiceStatus())
            .thenReturn(true)

        viewmodel.processInput(SensorViewEvents.OnStopViewEvent(true))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(it.isServiceRunning)
            true
        }

        viewEffectTester.assertValueAt(0) {
            assertThat(it is SensorViewEffects.UnbindService)
            true
        }
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never()).setServiceStatus(any())
    }

    @Test
    fun serviceNotRunning_onStopEvent_noEffect() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()
        `when`(repo.getServiceStatus())
            .thenReturn(false)

        viewmodel.processInput(SensorViewEvents.OnStopViewEvent(false))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(!it.isServiceRunning)
            true
        }

        viewEffectTester.assertValueCount(0)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never()).setServiceStatus(any())
    }

    @Test
    fun serviceRunning_notBounded_onStopEvent_noEffect() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()
        `when`(repo.getServiceStatus())
            .thenReturn(true)

        viewmodel.processInput(SensorViewEvents.OnStopViewEvent(false))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(it.isServiceRunning)
            true
        }

        viewEffectTester.assertValueCount(0)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never()).setServiceStatus(any())
    }

    @Test
    fun serviceNotRunning_bounded_onStopEvent_unBindService() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()
        `when`(repo.getServiceStatus())
            .thenReturn(false)

        viewmodel.processInput(SensorViewEvents.OnStopViewEvent(true))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(!it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueAt(0) {
            assertThat(it is SensorViewEffects.UnbindService)
            true
        }
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never()).setServiceStatus(any())
    }

    @Test
    fun bounded_stopRequest_stopService() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        viewmodel.processInput(SensorViewEvents.EndServiceEvent(
            isServiceBounded = true,
            isAllowedStorage = true
        ))


        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(!it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueAt(0) {
            assertThat(it is SensorViewEffects.StopService)
            true
        }
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
    }

    @Test
    fun notBounded_stopRequest_doNothing() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        viewmodel.processInput(SensorViewEvents.EndServiceEvent(
            isServiceBounded = false,
            isAllowedStorage = true
        ))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(!it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueCount(0)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never())
            .setServiceStatus(any())
    }

    @Test
    fun notBounded_noStoragePermission_stopRequest_doNothing() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        viewmodel.processInput(SensorViewEvents.EndServiceEvent(
            isServiceBounded = false,
            isAllowedStorage = true
        ))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(!it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueCount(0)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        verify(repo, never())
            .setServiceStatus(any())
    }


    @Test
    fun revokedStorage_serviceBounded_stopRequest() {
        viewmodel = SensorViewModel(repo)
        val viewEffectTester = viewmodel.viewEffect.test()
        val viewStateTester = viewmodel.viewState.test()

        viewmodel.processInput(SensorViewEvents.EndServiceEvent(
            isServiceBounded = true,
            isAllowedStorage = false
        ))

        viewStateTester.assertValueAt(0) {
            assertThat(!it.isProcessing)
            assertThat(it.isServiceRunning)
            true
        }
        viewEffectTester.assertValueCount(1)
        viewEffectTester.assertNoErrors()
        viewStateTester.assertNoErrors()
        viewEffectTester.assertValue {
            assertThat(it is SensorViewEffects.PermissionRequired)
            true
        }
        verify(repo, never())
            .setServiceStatus(any())
    }
}