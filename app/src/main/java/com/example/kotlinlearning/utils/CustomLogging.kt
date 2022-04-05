package com.example.kotlinlearning.utils

import android.util.Log
import com.example.kotlinlearning.BuildConfig
import com.google.gson.Gson

object CustomLogging {

    fun <T> normalLog(c: Class<T>, data: Any) {
        if (BuildConfig.DEBUG)
            Log.i(c.simpleName, "normalLog: ${Gson().toJson(data)} ")
    }

    fun <T> errorLog(c: Class<T>, data: Any) {
        if (BuildConfig.DEBUG)
            Log.e(c.simpleName, "normalLog: ${Gson().toJson(data)} ")
    }
}