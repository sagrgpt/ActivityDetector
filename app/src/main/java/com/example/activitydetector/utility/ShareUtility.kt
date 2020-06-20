package com.example.activitydetector.utility

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference

class ShareUtility(context: Context) {
    private val context: WeakReference<Context> = WeakReference(context)

    fun share(file: File): Boolean {
        if (context.get() == null)
            return false
        val intent = Intent(Intent.ACTION_SEND)
            .apply { type = "text/csv, text/comma-separated-values" }
        intent.putExtra(Intent.EXTRA_SUBJECT, "Latest Run Logs")
        intent.putExtra(Intent.EXTRA_TEXT, "PFA logs.")
        val contentUri = getFileUri(file)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        try {
            if (intent.resolveActivity(context.get()!!.packageManager) != null) {
                context.get()!!
                    .startActivity(Intent.createChooser(intent, "Send Feedback..."))
            }
        } catch (ex: ActivityNotFoundException) {
            Timber.w(ex, "No email client found")
            context.get()
                ?.let {
                    Toast.makeText(
                        it,
                        "No email client found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } finally {
            return true
        }
    }

    private fun getFileUri(file: File): Uri? {
        return context.get()
            ?.let {
                FileProvider.getUriForFile(
                    it,
                    "com.example.activitydetector.fileprovider",
                    file
                )
            }
    }

}