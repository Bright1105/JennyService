package com.example.jennyserviceapp.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.ServiceViewModelProvider
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    openAndPopUp: (ServiceDestinations, ServiceDestinations) -> Unit,
    splashViewModel: SplashViewModel = viewModel(factory = ServiceViewModelProvider.Factory),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }

    LaunchedEffect(key1 = true) {
        delay(1000L)
        splashViewModel.onProfile(openAndPopUp)
    }
}