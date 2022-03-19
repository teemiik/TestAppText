package com.example.testapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.usecases.TextDataUseCase

class MainViewModelFactory(private val textDataUseCase: TextDataUseCase):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(TextDataUseCase::class.java)
                .newInstance(textDataUseCase) as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}