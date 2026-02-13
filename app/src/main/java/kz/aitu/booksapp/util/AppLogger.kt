package kz.aitu.booksapp.util

import android.util.Log
import kz.aitu.booksapp.BuildConfig

object AppLogger {
    fun d(tag: String, msg: String) {
        if (BuildConfig.LOG_ENABLED) Log.d(tag, msg)
    }
    fun e(tag: String, msg: String, t: Throwable? = null) {
        if (BuildConfig.LOG_ENABLED) Log.e(tag, msg, t)
    }
}
