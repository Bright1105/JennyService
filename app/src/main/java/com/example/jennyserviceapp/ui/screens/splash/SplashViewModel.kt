package com.example.jennyserviceapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.jennyserviceapp.Accounts
import com.example.jennyserviceapp.Login
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.Splash
import com.example.jennyserviceapp.data.service.AccountService

class SplashViewModel(
    private val accountService: AccountService
) : ViewModel() {

    fun onProfile(openAndPopUp: (ServiceDestinations, ServiceDestinations) -> Unit) {
        if (accountService.hasUser()) openAndPopUp(Accounts, Splash)
        else openAndPopUp(Login, Splash)
    }
}