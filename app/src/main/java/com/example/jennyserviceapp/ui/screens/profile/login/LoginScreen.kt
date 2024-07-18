package com.example.jennyserviceapp.ui.screens.profile.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.ServiceViewModelProvider
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateAndPopup: (ServiceDestinations, ServiceDestinations) -> Unit,
    viewModel: LoginViewModel = viewModel(factory = ServiceViewModelProvider.Factory)
) {
    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val scope = rememberCoroutineScope()


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LoginTextField(
            value = email.value,
            onValueChanged = {  email ->
                viewModel.updateEmail(email)
            },
            labelName = stringResource(R.string.email)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5)))
        LoginTextField(
            value = password.value,
            onValueChanged = { password ->
                viewModel.updatePassword(password)
            },
            labelName = stringResource(R.string.password)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Button(
            onClick = {
                scope.launch {
                    viewModel.signIn(navigateAndPopup)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Text(
                text = stringResource(R.string.login)
            )
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    labelName: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = labelName,
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_10)
            )
    )
}