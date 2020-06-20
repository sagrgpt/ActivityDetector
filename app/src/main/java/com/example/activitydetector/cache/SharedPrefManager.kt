package com.example.activitydetector.cache

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(appContext: Context) {
    private val prefs: SharedPreferences

    init {
        prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun clearPrefs() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun removeKey(key: String?) {
        prefs
            .edit()
            .remove(key)
            .apply()
    }

    fun containsKey(key: String?): Boolean {
        return prefs.contains(key)
    }

    fun getString(key: String?, defValue: String?): String {
        return prefs.getString(key, defValue) ?: ""
    }

    fun getString(key: String?): String {
        return getString(key, null)
    }

    fun setString(key: String?, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getInt(key: String?, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun getInt(key: String?): Int {
        return getInt(key, 0)
    }

    fun setInt(key: String?, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getLong(key: String?, defValue: Long): Long {
        return prefs.getLong(key, defValue)
    }

    fun getLong(key: String?): Long {
        return getLong(key, 0L)
    }

    fun setLong(key: String?, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    fun setBoolean(key: String?, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getFloat(key: String?): Float {
        return getFloat(key, 0f)
    }

    fun getFloat(key: String?, defValue: Float): Float {
        return prefs.getFloat(key, defValue)
    }

    fun setFloat(key: String?, value: Float?) {
        val editor = prefs.edit()
        editor.putFloat(key, value!!)
        editor.apply()
    }

    val editor: SharedPreferences.Editor
        get() = prefs.edit()

    companion object {
        const val PREF_NAME = "com.example.activityDetector"
        private val uniqueInstance: SharedPrefManager? = null
    }
}