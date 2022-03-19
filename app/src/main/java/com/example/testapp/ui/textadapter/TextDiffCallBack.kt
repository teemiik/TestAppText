package com.example.testapp.ui.textadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.testapp.entities.TextData

class TextDiffCallBack : DiffUtil.ItemCallback<TextData>() {

    override fun areItemsTheSame(oldItem: TextData, newItem: TextData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TextData, newItem: TextData): Boolean {
        return oldItem.id == newItem.id
    }
}
