package com.example.activitydetector.mvvm.sensorScreen

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.activitydetector.R
import com.example.activitydetector.di.ViewModelFactory
import com.example.activitydetector.mvvm.adapter.FileListAdapter
import com.example.activitydetector.mvvm.common.activity.BaseActivity
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEffects
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEffects.*
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewEvents
import com.example.activitydetector.mvvm.sensorScreen.model.SensorViewState
import com.example.activitydetector.mvvm.sensorScreen.viewmodel.SensorViewModel
import com.example.activitydetector.utility.PermissionUtility
import com.example.activitydetector.utility.ShareUtility
import com.example.activitydetector.utility.fileManager.FileResourceGateway
import com.example.sensordatagenerator.interfaces.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_sensor.*
import timber.log.Timber
import javax.inject.Inject


class SensorActivity : BaseActivity() {

    private val disposable = CompositeDisposable()
    private var uiDisposable: Disposable? = null
    private var sensorService: SensorService? = null
    private lateinit var viewModel: SensorViewModel

    @Inject
    lateinit var permissionUtils: PermissionUtility

    @Inject
    lateinit var scheduler: SchedulerProvider

    @Inject
    lateinit var fileManager: FileResourceGateway

    @Inject
    lateinit var shareUtility: ShareUtility
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var listAdapter: FileListAdapter
    @Inject
    lateinit var itemClickListener: PublishSubject<String>

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.v("Connected to service")
            sensorService = (service as SensorService.MyBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.v("Service Disconnected")
            sensorService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SensorViewModel::class.java)
        setContentView(R.layout.activity_sensor)
        populateActivitySpinner()
        setUpListView()
        disposable.add(
            viewModel.viewState
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.ui)
                .subscribe(::render)
                { Timber.e(it, "Something went wrong with View State") }
        )

        disposable.add(
            viewModel.viewEffect
                .subscribeOn(scheduler.io)
                .observeOn(scheduler.ui)
                .subscribe(::trigger)
                { Timber.e(it, "Something went wrong with View Effect") }
        )

        setClickListener()

    }

    override fun onStart() {
        super.onStart()
        viewModel.processInput(SensorViewEvents.OnStartViewEvent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.processInput(SensorViewEvents.ScreenLoadEvent)
        uiDisposable = itemClickListener
            .subscribeOn(scheduler.io)
            .subscribe(
                ::shareFile
            ) {
                Timber.e(it, "Unable to share file")
                showToast(getString(R.string.sharable_error_toast))
            }
    }

    override fun onPause() {
        super.onPause()
        uiDisposable?.dispose()
    }

    override fun onStop() {
        super.onStop()
        viewModel.processInput(
            SensorViewEvents.OnStopViewEvent(sensorService != null)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun render(viewState: SensorViewState) {
        if (viewState.isServiceRunning) {
            start_service.visibility = View.GONE
            stop_service.visibility = View.VISIBLE
        } else {
            start_service.visibility = View.VISIBLE
            stop_service.visibility = View.GONE
        }
        viewState.nameList?.let {
            recycler_view.visibility = View.VISIBLE
            dataset_hint.visibility = View.GONE
            listAdapter.setData(it)
        } ?: let {
            recycler_view.visibility = View.GONE
            dataset_hint.visibility = View.VISIBLE
            listAdapter.setData(emptyList())
        }
    }

    private fun trigger(viewEffects: SensorViewEffects) {
        when (viewEffects) {
            BindService -> connectToService()
            UnbindService -> disconnectService()
            StartService -> {
                startService()
                connectToService()
            }
            StopService -> requestServiceClosure()
            PermissionRequired -> showToast(getString(R.string.permissions_required_toast))
        }
    }

    private fun shareFile(fileName: String) {
        fileManager.getShareable(fileName)
            ?.also { shareUtility.share(it) }
    }

    private fun startService() {
        Timber.v("Starting service")
        startService(Intent(this, SensorService::class.java))
    }

    private fun connectToService() {
        Timber.v("Binding to service")
        bindService(
            Intent(this, SensorService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun disconnectService() {
        Timber.v("unbinding from service")
        unbindService(serviceConnection)
        sensorService = null
    }

    private fun requestServiceClosure() {
        Timber.v("Stopping service")
        sensorService?.closeResources()
            ?.observeOn(scheduler.ui)
            ?.subscribe(
                ::serviceClosure
            ) {
                Timber.e(it, "Error while Stopping service")
                showToast(getString(R.string.service_error_toast))
            }
    }

    private fun serviceClosure() {
        Timber.v("Service resources cleared.")
        sensorService?.stopSelf()
        disconnectService()
        showToast(getString(R.string.service_end_toast_msg))
        viewModel.processInput(SensorViewEvents.ScreenLoadEvent)
    }

    private fun populateActivitySpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.activity_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activity_spinner.adapter = adapter
        }
    }

    private fun setUpListView() {
        val layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false)
        recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.list_divider_space)!!
        )
        recycler_view.addItemDecoration(dividerItemDecoration)

        recycler_view.adapter = listAdapter

    }

    private fun setClickListener() {
        stop_service.setOnClickListener {
            viewModel.processInput(SensorViewEvents.EndServiceEvent(
                sensorService != null,
                isExternalStorageGranted()
            ))
        }

        start_service.setOnClickListener {
            viewModel.processInput(
                SensorViewEvents.StartServiceEvent(
                    isExternalStorageGranted(),
                    activity_spinner.selectedItem.toString()
                ))
        }
    }

    private fun isExternalStorageGranted(): Boolean {
        return permissionUtils.askPermissionForActivity(
            this,
            WRITE_EXTERNAL_STORAGE,
            340
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}
