package com.example.testapp.ui.textadapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.entities.TextData
import com.example.testapp.databinding.TextItemBinding

class TextViewHolder(
    private val binding: TextItemBinding,
    private val clickItem: (text: TextData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TextData) {
        binding.textView3.text = item.value
        drawBackground(item.isSelected)

        binding.textView3.setOnClickListener {
            item.isSelected = !item.isSelected
            drawBackground(item.isSelected)
            clickItem(item)
        }
    }

    private fun drawBackground(isSelected: Boolean) {
        binding.textView3.setBackgroundColor(
            ContextCompat.getColor(binding.root.context, if (isSelected) R.color.purple_500 else R.color.teal_700)
        )
    }
}
