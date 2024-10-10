package com.juri.kolo_android.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody

fun <T> parseError(dataClass: Class<T>, body: ResponseBody): T? {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val errorAdapter = moshi.adapter(dataClass)
    val errorBodyString = body.string()

    val errorBody = try {
        errorAdapter.fromJson(errorBodyString)
    } catch (e: Exception) {
        null
    }

    return errorBody
}