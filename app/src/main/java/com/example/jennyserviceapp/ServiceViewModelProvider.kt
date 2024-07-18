package com.example.jennyserviceapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jennyserviceapp.ui.screens.notification.NotificationViewModel
import com.example.jennyserviceapp.ui.screens.profile.AccountViewModel
import com.example.jennyserviceapp.ui.screens.profile.login.LoginViewModel
import com.example.jennyserviceapp.ui.screens.promotions.PromotionsViewModel
import com.example.jennyserviceapp.ui.screens.splash.SplashViewModel
import com.example.jennyserviceapp.ui.screens.uploadfeed.FeedViewModel
import com.example.jennyserviceapp.ui.screens.uploadproducts.ProductsViewModel


/**
 * Provides Factory to create instance of viewModel for the entire Service App
 */
object ServiceViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            ProductsViewModel(
                repository = serviceApplication().container.repository
            )
        }

        initializer {
            FeedViewModel(
                repository = serviceApplication().container.repository
            )
        }

        initializer {
            PromotionsViewModel(
                repository = serviceApplication().container.repository
            )
        }

        initializer {
            LoginViewModel(
                repository = serviceApplication().container.repository,
                accountService = serviceApplication().container.accountService
            )
        }

        initializer {
            AccountViewModel(
                repository = serviceApplication().container.repository,
                accountService = serviceApplication().container.accountService
            )
        }

        initializer {
            SplashViewModel(accountService = serviceApplication().container.accountService)
        }

        initializer {
            NotificationViewModel(
                repository = serviceApplication().container.repository
            )
        }
    }
}


/**
 * Extension function to queries for [Application] object and returns an instance of [ServiceApplication]
 */
fun CreationExtras.serviceApplication(): ServiceApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ServiceApplication)