package com.example.testapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.App
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.ui.textadapter.TextAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }
    private lateinit var textAdapter: TextAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModelFactory = App.appComponent.provideMainActivityViewModelFactory()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        textAdapter = TextAdapter {
            val clipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                "text",
                viewModel.texts.value.asSequence()
                    .filter { textData -> textData.isSelected }
                    .joinToString("\n") { textData -> textData.value })
            clipboardManager.setPrimaryClip(clipData)
        }

        binding.rvFoundText.layoutManager = LinearLayoutManager(this)
        binding.rvFoundText.adapter = textAdapter
        setListeners()
    }

    private fun setListeners() {
        binding.bRun.setOnClickListener {
            viewModel.loadTexts(this, binding.etUrl.text.toString(), binding.etQuery.text.toString())
        }

        lifecycleScope.launch {
            viewModel.texts
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    textAdapter.submitList(state)
                }
        }

        lifecycleScope.launch {
            viewModel.error
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    if (state.isNotEmpty())
                        Toast.makeText(this@MainActivity, state, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(MAIN_STATE_KEY, MainState(binding.etUrl.text.toString(), binding.etQuery.text.toString()))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val mainState = savedInstanceState.getParcelable<MainState>(MAIN_STATE_KEY)
        with(binding) {
            etUrl.setText(mainState?.url ?: "")
            etQuery.setText(mainState?.query ?: "")
        }
    }

    companion object {
        private const val MAIN_STATE_KEY = "main_state_key"
    }
}