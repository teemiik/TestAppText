package com.example.testapp.di

import com.example.testapp.network.TextDataSource
import com.example.testapp.network.TextDataSourceImpl
import com.example.testapp.repositories.TextDataRepository
import com.example.testapp.repositories.TextDataRepositoryImpl
import com.example.testapp.ui.MainViewModelFactory
import com.example.testapp.usecases.TextDataUseCase
import com.example.testapp.usecases.TextDataUseCaseImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppComponent {
    private fun provideDataSource(): TextDataSource =
        TextDataSourceImpl.getInstance()

    private fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    private fun provideRepository(): TextDataRepository {
        val dataSource = provideDataSource()
        val dispatcher = provideIODispatcher()
        return TextDataRepositoryImpl.getInstance(dataSource, dispatcher)
    }

    private fun provideUseCase(): TextDataUseCase {
        val repository = provideRepository()
        return TextDataUseCaseImpl.getInstance(repository)
    }

    fun provideMainActivityViewModelFactory(): MainViewModelFactory {
        val useCase = provideUseCase()
        return MainViewModelFactory(useCase)
    }
}