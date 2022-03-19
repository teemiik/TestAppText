package com.example.testapp.repositories

import com.example.testapp.network.TextDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.coroutineContext

interface TextDataRepository {
    fun getText(url: String, query: String, fileLogs: File): Flow<String>
}

class TextDataRepositoryImpl(
    private val textDataSource: TextDataSource,
    private val dispatcher: CoroutineDispatcher
): TextDataRepository {
    override fun getText(url: String, query: String, fileLogs: File): Flow<String> {
        return flow {
            var char: Char
            val line = StringBuilder()
            val inputStream = textDataSource.getText(url)
            inputStream.use { input ->
                while (input.read().also { char = it.toChar() } != -1 && coroutineContext.isActive) {
                    if (char == '\n') {
                        line.append(char)
                        val currentLine = line.toString()
                        if (isValidLine(currentLine, query)) {
                            writeLogs(currentLine, query, fileLogs)
                            emit(currentLine)
                        }
                        line.clear()
                    } else {
                        line.append(char)
                    }
                }
            }
        }.flowOn(dispatcher)
    }

    private fun isValidLine(line: String, query: String): Boolean {
        val regex = getCorrectRegex(query)
        return regex.containsMatchIn(line)
    }

    private fun writeLogs(line: String, query: String, fileLogs: File) {
        val regex = getCorrectRegex(query)
        val result = regex.findAll(line).joinToString("\n") { result -> result.value }
        if (result.isNotEmpty()) {
            val outputStream = FileOutputStream(fileLogs, true)
            outputStream.use { output ->
                result.forEach { ch ->
                    output.write(ch.code)
                }
            }
        }
    }

    private fun getCorrectRegex(query: String): Regex =
        "^${query.replace("*", ".+").replace("?", ".")}$".toRegex()

    companion object {
        private val LOCK = Any()

        @Volatile
        private var instance: TextDataRepositoryImpl? = null

        fun getInstance(textDataSource: TextDataSource, dispatcher: CoroutineDispatcher): TextDataRepositoryImpl {
            if (instance == null) {
                synchronized(LOCK) {
                    if (instance == null) {
                        instance = TextDataRepositoryImpl(textDataSource, dispatcher)
                    }
                }
            }
            return requireNotNull(instance)
        }
    }
}