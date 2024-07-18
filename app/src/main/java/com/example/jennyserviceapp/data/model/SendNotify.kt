package com.example.jennyserviceapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class SendNotify(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val dateCreated: Timestamp
)
