package com.example.testapp.entities

import java.util.*

class TextData(
    val value: String,
    val id: UUID = UUID.randomUUID(),
    var isSelected: Boolean = false
)