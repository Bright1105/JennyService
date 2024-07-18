package com.example.jennyserviceapp.data.model

import android.net.Uri

data class User(
    val id: String = "",
    val displayName: String? = null,
    val photoUri: Uri? = null,
    val email: String? = null,
    val emailVerified: Boolean = false,
)