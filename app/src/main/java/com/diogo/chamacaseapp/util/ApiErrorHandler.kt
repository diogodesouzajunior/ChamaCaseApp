package com.diogo.chamacaseapp.util

import android.content.Context
import com.diogo.chamacaseapp.R
import retrofit2.adapter.rxjava.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException



class ApiErrorHandler

constructor(var context: Context) {
    fun handleException(e: Throwable): String {
        val message: String

        if (e is HttpException) {
            val errorCode = e.code()
            message = handleErrorCode(errorCode)
        }else if(e is SocketTimeoutException){
            message = context.getString(R.string.request_timed_out)
        }else if(e is ConnectException){
            message = context.getString(R.string.failed_to_connect)
        }else{
            message = e.message ?: HttpStatus.UNKNOW.message
        }
        return message
    }

    private fun handleErrorCode(errorCode: Int): String {
        return HttpStatus.find(errorCode).message
    }

}