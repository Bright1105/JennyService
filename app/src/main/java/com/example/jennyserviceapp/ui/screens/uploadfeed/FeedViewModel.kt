package com.example.jennyserviceapp.ui.screens.uploadfeed

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.jennyserviceapp.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update



class FeedViewModel(
    private val repository: Repository
) : ViewModel() {

    var videoUri = MutableStateFlow<Uri?>(null)


    suspend fun addVideoToStorage() {
        videoUri.value?.let { repository.addVideoToFirebaseStorage(it) }
    }

    suspend fun addFeedToFireStore() {
        repository.addFeed()
    }
}