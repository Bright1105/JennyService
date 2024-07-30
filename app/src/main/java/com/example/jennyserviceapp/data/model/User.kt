package com.example.jennyserviceapp.data.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class User(
    val id: String = "",
    val displayName: String? = null,
    val photoUri: Uri? = null,
    val email: String? = null,
    val emailVerified: Boolean = false,
)

// Work Manager

data class JennyAccountInfo(
    @DocumentId
    val id: String = "",
    val firstName: String = "",
    val middleName: String? = "",
    val lastName: String = "",
    val bankName: String = "",
    val bankAccount: String = "",
    val phoneNumber: String = ""
)

data class Checkout(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val itemImage: String = "",
    val orderPending: Boolean = true,
    val orderReceived: Boolean = false,
    val cancel: Boolean = false,
    val dateCreated: Timestamp? = null
)

data class CheckoutCancel(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val itemImage: String = "",
    val reason: String = "",
    val cancel: Boolean = true,
    val dateCreated: Timestamp? = null
)

data class UserInformation(
    @DocumentId
    val id: String? = "",
    val userId: String? = "",
    val image: String? = null,
    val firstName: String? = "",
    val middleName: String? = "",
    val lastName: String? = "",
    val gender: String? = "",
    val phoneNumber: String? = "",
)

data class UserAddress(
    @DocumentId
    val id: String? = "",
    val userId: String? = "",
    val address: String? = "",
    val additionalInformation: String? = "",
    val region: String? = "",
    val city: String? = ""
)