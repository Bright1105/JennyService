package com.example.jennyserviceapp.data

import android.content.Context
import com.example.jennyserviceapp.data.repository.Repository
import com.example.jennyserviceapp.data.repository.RepositoryImpl
import com.example.jennyserviceapp.data.service.AccountService
import com.example.jennyserviceapp.data.service.AccountServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage


interface AppContainer {

    val repository: Repository
    val accountService: AccountService
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val firestore = Firebase.firestore

    private val storage = Firebase.storage

    override val repository: Repository by lazy {
        RepositoryImpl(
            firestore = firestore,
            firebaseStorage = storage,
            accountService = accountService
        )
    }

    override val accountService: AccountService by lazy {
        AccountServiceImpl()
    }
}