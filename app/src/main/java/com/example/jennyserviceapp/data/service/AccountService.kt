package com.example.jennyserviceapp.data.service

import android.net.Uri
import com.example.jennyserviceapp.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun updateProfile(displayName: String, photoUri: Uri?)
    fun getUser(): User?
}

class AccountServiceImpl() : AccountService {

    override suspend fun updateProfile(displayName: String, photoUri: Uri?) {
        val profile = userProfileChangeRequest {
            this.displayName = displayName
            this.photoUri = photoUri
        }
        Firebase.auth.currentUser!!
            .updateProfile(profile)
    }

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override fun getUser(): User? {
        return Firebase.auth.currentUser?.let { user ->
            User(
                displayName = user.displayName,
                photoUri = user.photoUrl,
                email = user.email,
                emailVerified = user.isEmailVerified
            )
        }
    }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String) {
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

}