package com.example.jennyserviceapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jennyserviceapp.HomeUpload
import com.example.jennyserviceapp.ServiceNavHost
import com.example.jennyserviceapp.ServiceViewModelProvider
import com.example.jennyserviceapp.StoreAppState
import com.example.jennyserviceapp.serviceTabColumnBar
import com.example.jennyserviceapp.ui.screens.profile.AccountViewModel


@Composable
fun ServiceApp() {
    val appState = rememberAppState()

    val currentBackStack by appState.navController.currentBackStackEntryAsState()
    val currentDestinations = currentBackStack?.destination
    val currentScreen =
        serviceTabColumnBar.find { it.route == currentDestinations?.route } ?: HomeUpload

    val accountViewModel: AccountViewModel = viewModel(factory = ServiceViewModelProvider.Factory)


    ServiceNavHost(
        appState,
        currentScreen = currentScreen,
        allScreen = serviceTabColumnBar,
        navigateScreens = {
            appState.navigate(it.route)
        }
    )
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        StoreAppState(navController)
    }