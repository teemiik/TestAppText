package com.example.testapp.usecases

import com.example.testapp.repositories.TextDataRepository
import com.example.testapp.util.SingletonWithArg
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TextDataUseCase {
    fun execute(url: String, query: String, fileLogs: File): Flow<String>
}

class TextDataUseCaseImpl(private val repository: TextDataRepository): TextDataUseCase {
    override fun execute(url: String, query: String, fileLogs: File): Flow<String> {
        return repository.getText(url, query, fileLogs)
    }

    companion object : SingletonWithArg<TextDataUseCaseImpl, TextDataRepository>(::TextDataUseCaseImpl)
}
