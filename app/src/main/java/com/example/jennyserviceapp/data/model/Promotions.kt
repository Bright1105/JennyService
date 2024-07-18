package com.example.jennyserviceapp.data.model

import com.google.firebase.firestore.DocumentId


data class Promotions(
    @DocumentId
    val id: String = "",
    val image: String = "",
    val name: String = "",
    val discount: String = "",
    val validDays: String = "",
    val worth: String = ""
)
