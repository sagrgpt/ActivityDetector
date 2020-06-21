package com.example.activitydetector.cache

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(appContext: Context) : CacheGateway {
    private val prefName = "com.example.activityDetector"

    private val prefs: SharedPreferences

    init {
        prefs = appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun clearPrefs() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    override fun removeKey(key: String?) {
        prefs
            .edit()
            .remove(key)
            .apply()
    }

    override fun containsKey(key: String?): Boolean {
        return prefs.contains(key)
    }

    override fun getString(key: String?, defValue: String?): String {
        return prefs.getString(key, defValue) ?: ""
    }

    override fun getString(key: String?): String {
        return getString(key, null)
    }

    override fun setString(key: String?, value: String?) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    override fun getInt(key: String?): Int {
        return getInt(key, 0)
    }

    override fun setInt(key: String?, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return prefs.getLong(key, defValue)
    }

    override fun getLong(key: String?): Long {
        return getLong(key, 0L)
    }

    override fun setLong(key: String?, value: Long) {
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    override fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    override fun setBoolean(key: String?, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun getFloat(key: String?): Float {
        return getFloat(key, 0f)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return prefs.getFloat(key, defValue)
    }

    override fun setFloat(key: String?, value: Float?) {
        val editor = prefs.edit()
        editor.putFloat(key, value!!)
        editor.apply()
    }
}