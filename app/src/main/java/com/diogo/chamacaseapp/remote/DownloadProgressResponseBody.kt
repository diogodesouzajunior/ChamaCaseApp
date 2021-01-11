package com.diogo.chamacaseapp.data.remote

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException



class DownloadProgressResponseBody(private val responseBody: ResponseBody,
                                   private val progressListener: DownloadProgressListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                }

                progressListener?.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)

                return bytesRead
            }
        }

    }
}