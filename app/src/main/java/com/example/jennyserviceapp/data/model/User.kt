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

data class JennyInfo(
    val firstName: String = "",
    val middleName: String? = "",
    val lastName: String = "",
    val bankName: String = "",
    val bankImage: String? = null,
    val bankAccount: String = "",
    val phoneNumber: String = ""
)

data class Checkout(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val ordersEntity: OrdersEntity = OrdersEntity(),
    val orderPending: Boolean = true,
    val orderReceived: Boolean = false,
    val dateCreated: Timestamp? = null
)


data class OrdersEntity(
    val id: String = "",
    val title: String = "",
    val brand: String = "",
    val countItem: Int = 0,
    val price: Int = 0,
    val description: String? = null,
    val itemType: String? = null,
    val dateCreated: String? = null,
    val image: String = "",
    // val itemAvailable: Int,
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