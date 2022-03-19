package com.example.testapp.network

import com.example.testapp.util.Singleton
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface TextDataSource {
    suspend fun getText(url: String): InputStream
}

class TextDataSourceImpl : TextDataSource {
    override suspend fun getText(url: String): InputStream = suspendCancellableCoroutine { continuation ->
        var connection: HttpURLConnection? = null
        try {
            val currentUrl = URL(url)
            connection = currentUrl.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                continuation.resumeWithException(IOException("Server returned HTTP " + connection.responseCode
                    .toString() + " " + connection.responseMessage
                ))
            }

            continuation.resume(connection.inputStream)
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }

        continuation.invokeOnCancellation {
            it?.printStackTrace()
            connection?.disconnect()
        }
    }

    companion object : Singleton<TextDataSourceImpl>(::TextDataSourceImpl)
}