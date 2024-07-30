package com.example.jennyserviceapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.Splash
import com.example.jennyserviceapp.data.model.JennyAccountInfo
import com.example.jennyserviceapp.data.model.User
import com.example.jennyserviceapp.data.repository.Repository
import com.example.jennyserviceapp.data.service.AccountService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: Repository,
    private val accountService: AccountService
) : ViewModel() {

    val displayName = MutableStateFlow("")
    val photoUri = MutableStateFlow<Uri?>(null)

    val userInfo: User? = accountService.getUser()
    val bankInfo: Flow<JennyAccountInfo?> = repository.getBankInfo

    val firstName = MutableStateFlow("")
    val middleName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val bankName = MutableStateFlow("")
    val bankAccount = MutableStateFlow("")
    val phoneNumber = MutableStateFlow("")

    fun initialize(restartApp: (ServiceDestinations) -> Unit) {
        viewModelScope.launch {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(Splash)
            }
        }
    }

    fun updateFirstName(firstName: String) {
        this.firstName.value = firstName
    }
    fun updateMiddle(middleName: String) {
        this.middleName.value = middleName
    }
    fun updateLastName(lastName: String) {
        this.lastName.value = lastName
    }
    fun updateBankAccount(bankAccount: String) {
        this.bankAccount.value = bankAccount
    }
    fun updateBankName(bankName: String) {
        this.bankName.value = bankName
    }
    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber.value = phoneNumber
    }

    suspend fun accountInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val accountInfo = JennyAccountInfo(
                firstName = firstName.value,
                middleName = middleName.value,
                lastName = lastName.value,
                bankAccount = bankAccount.value,
                bankName = bankName.value,
                phoneNumber = phoneNumber.value
            )
            repository.createBankInfo(accountInfo)
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