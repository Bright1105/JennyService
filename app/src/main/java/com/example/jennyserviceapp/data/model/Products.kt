package com.example.jennyserviceapp.data.model

import android.net.Uri
import com.google.firebase.Timestamp

data class Products(
    val name: String? = null,
    val price: Int? = null,
    val description: String? = null,
    val brand: String? = null,
    val itemType: String? = null,
    val imageUri: List<Uri?>? = null,
    val itemAvailable: Int? = null,
    val dateCreated: Timestamp? = null
)



data class Feeds(
    val videoUri: Uri? = null
)
