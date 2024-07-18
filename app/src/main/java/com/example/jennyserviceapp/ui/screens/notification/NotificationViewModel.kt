package com.example.jennyserviceapp.ui.screens.notification

import androidx.lifecycle.ViewModel
import com.example.jennyserviceapp.data.model.SendNotify
import com.example.jennyserviceapp.data.repository.Repository
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: Repository
) : ViewModel() {

    val title = MutableStateFlow("")
    val message = MutableStateFlow("")


    suspend fun sendNotify() {
        CoroutineScope(Dispatchers.IO).launch {
            val notify = SendNotify(
                title = title.value,
                message = message.value,
                dateCreated = Timestamp.now()
            )
            runCatching {
                repository.sendNotify(notify)
            }
            clean()
        }
    }

    fun clean() {
        title.value = ""
        message.value = ""
    }

    fun updateTitle(title: String) {
        this.title.value = title
    }

    fun updateMessage(message: String) {
        this.message.value = message
    }
}