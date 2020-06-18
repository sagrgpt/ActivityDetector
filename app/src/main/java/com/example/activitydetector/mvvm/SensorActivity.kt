package com.example.activitydetector.mvvm

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.activitydetector.R
import com.example.activitydetector.cache.PreferenceConstants
import com.example.activitydetector.cache.SharedPrefManager
import com.example.activitydetector.mvvm.common.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_sensor.*
import net.ozaydin.serkan.easy_csv.EasyCsv
import net.ozaydin.serkan.easy_csv.FileCallback
import java.io.File
import javax.inject.Inject


class SensorActivity : BaseActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private lateinit var accelerometer: Sensor

    private lateinit var gyroscope: Sensor

    private var bounded = false

    private var accelerometerAccuracy = ""
    private var gyroAccuracy = ""

    private var gravity = mutableListOf(0f, 0f, 0f)

    private val alpha: Float = 0.8f

    private val dataList: MutableList<String> = mutableListOf()

    private lateinit var easyCsv: EasyCsv

    private var sensorService: SensorService? = null

    private var serviceBounded = false

    private val preferenceConstants = PreferenceConstants()

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.v("SensorActivity", "Connected to service")
            serviceBounded = true
            sensorService = (service as SensorService.MyBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.v("SensorActivity", "Service Disconnected")
            sensorService = null
            serviceBounded = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        setContentView(R.layout.activity_sensor)
        /*sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        easyCsv = EasyCsv(this)*/
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
        Log.v("SensorActivity", "Stopping service")
        sensorService?.stop()
        sensorService = null
        serviceBounded = false
        disconnectService()
        sharedPrefManager.setBoolean(
            preferenceConstants.isServiceRunning,
            false
        )
    }

    private fun saveCsv() {
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
    }

    override fun onStart() {
        super.onStart()
        /*if(!bounded) {
            val accBounded = sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME
            )
            val gyrBounded = sensorManager.registerListener(
                this,
                gyroscope,
                SensorManager.SENSOR_DELAY_GAME
            )
            bounded = accBounded || gyrBounded
        }*/
        if (isRequiredToBindService()) {
            bindToService()
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

    override fun onStop() {
        super.onStop()
        /* if(bounded)
             sensorManager.unregisterListener(this)*/
        bounded = false
        if (serviceBounded) {
            disconnectService()
        }
    }

    private fun startService() {
        Log.v("SensorActivity", "Starting service")
        startService(Intent(this, SensorService::class.java))
    }

    private fun bindToService() {
        Log.v("SensorActivity", "Binding to service")
        bindService(
            Intent(this, SensorService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun disconnectService() {
        Log.v("SensorActivity", "unbinding from service")
        unbindService(serviceConnection)
        sensorService = null
        serviceBounded = false
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        sensor?.also {
            if (it.type == Sensor.TYPE_ACCELEROMETER)
                accelerometerAccuracy = onAccuracyChanged(accuracy)
            else if (it.type == Sensor.TYPE_GYROSCOPE)
                gyroAccuracy = onAccuracyChanged(accuracy)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.also {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                onDataPoint(AccelerometerData(
                    x = it.values[0],
                    y = it.values[1],
                    z = it.values[2],
                    timestamp = it.timestamp
                ))
            } else if (it.sensor.type == Sensor.TYPE_GYROSCOPE) {
                onDataPoint(GyroData(
                    x = it.values[0],
                    y = it.values[1],
                    z = it.values[0],
                    timestamp = it.timestamp
                ))
            }
        }
    }

    private fun onAccuracyChanged(accuracy: Int): String = when (accuracy) {
        SensorManager.SENSOR_STATUS_UNRELIABLE -> "unreliable"
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "high"
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "low"
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "medium"
        else -> "unknown"
    }


    private fun onDataPoint(data: GyroData) {
        data.also {
            Log.v("Gyroscope",
                "$gyroAccuracy accuracy data: ${it.x}|${it.y}|${it.z}/")
        }
    }

    private fun onDataPoint(data: AccelerometerData) {
        data.lowPassFilter()
            .also {
                Log.v("SensorActivity",
                    "$accelerometerAccuracy accuracy data: ${it.x}|${it.y}|${it.z}/")
                if (accelerometerAccuracy == "high") {
                    dataList.add("${it.timestamp}|${it.x}|${it.y}|${it.z}/")
                }
            }
    }

    private fun AccelerometerData.lowPassFilter(): AccelerometerData {
        gravity[0] = alpha * gravity[0] + (1 - alpha) * x
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z

        return copy(
            x = x - gravity[0],
            y = y - gravity[1],
            z = z - gravity[2]
        )
    }

    data class GyroData(
        val x: Float = 0f,
        val y: Float = 0f,
        val z: Float = 0f,
        val timestamp: Long
    )

    data class AccelerometerData(
        val x: Float = 0f,
        val y: Float = 0f,
        val z: Float = 0f,
        val timestamp: Long
    )

}
