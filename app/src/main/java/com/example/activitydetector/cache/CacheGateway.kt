package com.example.activitydetector.cache

interface CacheGateway {
    fun clearPrefs()
    fun removeKey(key: String?)
    fun containsKey(key: String?): Boolean
    fun getString(key: String?, defValue: String?): String
    fun getString(key: String?): String
    fun setString(key: String?, value: String?)
    fun getInt(key: String?, defValue: Int): Int
    fun getInt(key: String?): Int
    fun setInt(key: String?, value: Int)
    fun getLong(key: String?, defValue: Long): Long
    fun getLong(key: String?): Long
    fun setLong(key: String?, value: Long)
    fun getBoolean(key: String?, defValue: Boolean): Boolean
    fun getBoolean(key: String?): Boolean
    fun setBoolean(key: String?, value: Boolean)
    fun getFloat(key: String?): Float
    fun getFloat(key: String?, defValue: Float): Float
    fun setFloat(key: String?, value: Float?)
}