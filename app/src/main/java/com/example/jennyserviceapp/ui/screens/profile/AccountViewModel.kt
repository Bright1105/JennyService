package com.example.jennyserviceapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.Splash
import com.example.jennyserviceapp.data.model.User
import com.example.jennyserviceapp.data.repository.Repository
import com.example.jennyserviceapp.data.service.AccountService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: Repository,
    private val accountService: AccountService
) : ViewModel() {

    val displayName = MutableStateFlow("")
    val photoUri = MutableStateFlow<Uri?>(null)

    val userInfo: User? = accountService.getUser()

    fun initialize(restartApp: (ServiceDestinations) -> Unit) {
        viewModelScope.launch {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(Splash)
            }
        }
    }

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                accountService.updateProfile(displayName.value, photoUri.value)
            }
        }
    }

    fun updateDisplayName(displayName: String) {
        this.displayName.value = displayName
    }
}