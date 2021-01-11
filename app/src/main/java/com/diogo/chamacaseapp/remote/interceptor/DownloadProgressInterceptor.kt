package com.diogo.chamacaseapp.remote.interceptor

import com.diogo.chamacaseapp.data.remote.DownloadProgressListener
import com.diogo.chamacaseapp.data.remote.DownloadProgressResponseBody
import com.diogo.chamacaseapp.data.remote.SimpleBusEvent
import com.diogo.chamacaseapp.remote.interceptor.model.ProgressEvent
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class DownloadProgressInterceptor(val eventBus: SimpleBusEvent): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val isAStream = chain.request().header("content-type") == "application/octet-stream"

        if(isAStream) {
            val originalResponse = chain.proceed(chain.request())
            val builder = originalResponse.newBuilder()

                builder.body(originalResponse.body()?.let {
                    DownloadProgressResponseBody(it, object : DownloadProgressListener {
                        override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                            Timber.d("BytesRead: $bytesRead, contentLength: $contentLength, done: $done")
                            eventBus.post(ProgressEvent(contentLength, bytesRead))
                        }
                    })
                })



            return builder.build()

        }

        val request = chain.request()
        val newRequest = request?.newBuilder()
        return chain.proceed(newRequest?.build())!!
    }
}