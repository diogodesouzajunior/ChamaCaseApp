package com.diogo.chamacaseapp.remote.interceptor

import android.database.sqlite.SQLiteException
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        val newRequest = request?.newBuilder()

        try {
        } catch (ex: SQLiteException) {
        }

        return chain?.proceed(newRequest?.build())!!
    }
}