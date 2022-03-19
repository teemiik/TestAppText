package com.example.testapp.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MainState(
    val url: String,
    val query: String
): Parcelable