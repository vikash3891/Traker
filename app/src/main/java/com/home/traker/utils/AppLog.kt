package com.home.traker.utils

object AppLog {
    val TAG = "Traker_logs"

    val LOG_ENABLED = true

    fun printLog(message: String) {
        if (LOG_ENABLED)
            android.util.Log.e(TAG, message)
    }

    fun printLog(tag: String, message: String) {
        if (LOG_ENABLED)
            android.util.Log.e(tag, message)
    }
}
