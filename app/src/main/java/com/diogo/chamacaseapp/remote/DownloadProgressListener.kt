package com.diogo.chamacaseapp.data.remote


interface DownloadProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}