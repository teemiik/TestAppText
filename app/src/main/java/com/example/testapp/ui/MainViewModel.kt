package com.example.testapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.R
import com.example.testapp.entities.TextData
import com.example.testapp.usecases.TextDataUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainViewModel(
    private val textUseCase: TextDataUseCase
) : ViewModel() {

    private val internalTexts = LinkedList<TextData>()

    private var loadTextJob: Job? = null

    private val _texts = MutableStateFlow<List<TextData>>(emptyList())
    val texts: StateFlow<List<TextData>> = _texts

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadTexts(context: Context, url: String, query: String) {
        if (url.isEmpty() || query.isEmpty()) {
            _error.value = context.getString(R.string.empty_fields)
            return
        }

        loadTextJob?.cancel()
        internalTexts.clear()
        _texts.value = emptyList()
        loadTextJob = viewModelScope.launch {
            val fileLogs = context.createFile(NAME_FILE_LOGS, EXTENSION_FILE_LOGS)
            textUseCase.execute(url, query, fileLogs)
                .catch { e -> _error.value = e.message ?: context.getString(R.string.unknown_error) }
                .collect {
                    internalTexts.add(TextData(it))
                    _texts.value = internalTexts.toList()
                }
        }
    }

    private fun Context.createFile(name: String, extension: String): File {
        val dir = File(this.cacheDir, TEMP_LOGS_DIRECTORY).apply { mkdirs() }
        val file = File(dir, "$name.$extension").apply {
            if (!exists()) {
                createNewFile()
            }
        }

        return file
    }

    companion object {
        private const val TEMP_LOGS_DIRECTORY = "logs"
        private const val NAME_FILE_LOGS = "results"
        private const val EXTENSION_FILE_LOGS = "log"
    }
}