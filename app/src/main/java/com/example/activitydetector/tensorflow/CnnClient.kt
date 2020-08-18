package com.example.activitydetector.tensorflow

import android.content.Context
import android.content.res.AssetManager
import com.example.activitydetector.tensorflow.store.TensorData
import org.tensorflow.lite.Interpreter
import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class CnnClient(
    private val context: Context
) {

    private var tfLite: Interpreter? = null

    fun load() {
        loadModel()
    }

    fun unload() {
        tfLite?.close()
    }

    fun predict(dataList: List<TensorData>): Int {
        val input = arrayOf<Array<Float>>()
        for (i in dataList.indices) {
            input[i] = arrayOf(
                dataList[i].x,
                dataList[i].y,
                dataList[i].z,
                dataList[i].x2,
                dataList[i].y2,
                dataList[i].z2
            )
        }
        val output = arrayOf<Int>()
        tfLite?.run(input, output)
        return output[0]
    }

    private fun loadModel() {
        try {
            val buffer: ByteBuffer = loadModelFile(context.assets)
            tfLite = Interpreter(buffer)
            Timber.v("TFLite Model loaded")
        } catch (ex: IOException) {
            Timber.e(ex, "Unable to load TFLite model")
        }
    }

    private fun loadModelFile(assets: AssetManager?): ByteBuffer {
        assets?.openFd(CNN_PATH)
            ?.use { fileDescriptor ->
                FileInputStream(fileDescriptor.fileDescriptor)
                    .use { inputStream ->
                        return inputStream.channel.map(
                            FileChannel.MapMode.READ_ONLY,
                            fileDescriptor.startOffset,
                            fileDescriptor.declaredLength
                        )
                    }
            }
            ?: throw RuntimeException("Asset manager not available.")
    }

}