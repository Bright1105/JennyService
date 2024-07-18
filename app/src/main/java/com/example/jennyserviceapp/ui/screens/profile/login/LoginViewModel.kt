package com.example.jennyserviceapp.ui.screens.profile.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jennyserviceapp.Accounts
import com.example.jennyserviceapp.Login
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.Splash
import com.example.jennyserviceapp.data.repository.Repository
import com.example.jennyserviceapp.data.service.AccountService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: Repository,
    private val accountService: AccountService
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")



    suspend fun signIn(navigateAndPopup: (ServiceDestinations, ServiceDestinations) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                accountService.signIn(email.value, password.value)
                navigateAndPopup(Accounts, Login)
            }
        }
    }

    fun updateEmail(email: String) {
        this.email.value = email
    }

    fun updatePassword(password: String) {
        this.password.value = password
    }
}