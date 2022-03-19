package com.example.testapp.ui.textadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.testapp.entities.TextData
import com.example.testapp.databinding.TextItemBinding

class TextAdapter(
    private val clickItem: (text: TextData) -> Unit
) : ListAdapter<TextData, TextViewHolder>(TextDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val binding = TextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextViewHolder(
            binding = binding,
            clickItem = clickItem
        )
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
