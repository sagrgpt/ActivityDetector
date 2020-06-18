package com.example.activitydetector.mvvm

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.example.activitydetector.R
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import com.example.activitydetector.mvvm.common.activity.BaseActivity
import com.example.activitydetector.sensors.PermissionUtility
import kotlinx.android.synthetic.main.activity_sensor.*
import timber.log.Timber
import javax.inject.Inject


class SensorActivity : BaseActivity() {

    /*private val dataList: MutableList<String> = mutableListOf()

    private lateinit var easyCsv: EasyCsv*/

    private var sensorService: SensorService? = null

    private var serviceBounded = false

    @Inject
    lateinit var preferenceConstants: PreferenceConstants

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    @Inject
    lateinit var permissionUtils: PermissionUtility

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Timber.v("Connected to service")
            serviceBounded = true
            sensorService = (service as SensorService.MyBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.v("Service Disconnected")
            sensorService = null
            serviceBounded = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView(R.layout.activity_sensor)
        /*easyCsv = EasyCsv(this)*/
        stop_service.setOnClickListener { stopService() }
        start_service.setOnClickListener {
            startService()
            bindToService()
            sharedPrefManager.setBoolean(
                preferenceConstants.isServiceRunning,
                true
            )
        }
    }

    private fun stopService() {
        if (isExternalStorageGranted()) {
            Timber.v("Stopping service")
            sensorService?.stop()
            sensorService = null
            serviceBounded = false
            disconnectService()
            sharedPrefManager.setBoolean(
                preferenceConstants.isServiceRunning,
                false
            )
        }
    }

    private fun isExternalStorageGranted(): Boolean {
        return permissionUtils.askPermissionForActivity(
            this,
            WRITE_EXTERNAL_STORAGE,
            340
        )
    }

    /*private fun saveCsv() {
        val fileName = "accelerometerDataSet"
        val header: MutableList<String> = mutableListOf()
        header.add("Timestamp|X|Y|Z/")

        easyCsv.setSeparatorColumn("|")
        easyCsv.setSeperatorLine("/")

        easyCsv.createCsvFile(
            fileName,
            header,
            dataList,
            1001,
            object : FileCallback {
                override fun onSuccess(p0: File?) {
                    Toast.makeText(
                        this@SensorActivity,
                        "File Saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFail(p0: String?) {
                    Toast.makeText(
                        this@SensorActivity,
                        "Unable to save file",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }*/

    override fun onStart() {
        super.onStart()
        if (isRequiredToBindService()) {
            bindToService()
        }
    }

    override fun onStop() {
        super.onStop()
        if (serviceBounded) {
            disconnectService()
        }
    }

    private fun isRequiredToBindService(): Boolean {
        return sharedPrefManager.getBoolean(
            preferenceConstants.isServiceRunning,
            false
        )
            .let { isServiceRunning ->
                isServiceRunning && !serviceBounded
            }
    }

    private fun startService() {
        Timber.v("Starting service")
        startService(Intent(this, SensorService::class.java))
    }

    private fun bindToService() {
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
        serviceBounded = false
    }

}
