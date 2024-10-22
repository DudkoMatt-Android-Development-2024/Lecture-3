package com.github.dudkomatt.androidcourse.lecture3contactlist.data

import androidx.compose.runtime.Immutable

@Immutable
data class Contact(
    val idx: Int,
    val name: String,
    val phoneNumber: String
)
