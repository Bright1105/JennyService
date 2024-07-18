package com.example.jennyserviceapp.ui.screens.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(
    notificationViewModel: NotificationViewModel = viewModel(factory = ServiceViewModelProvider.Factory)
) {

    val title = notificationViewModel.title.collectAsState()
    val message = notificationViewModel.message.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        NotifyTextField(
            value = title.value,
            onValueChanged = { title ->
                notificationViewModel.updateTitle(title)
            },
            placeHolderName = stringResource(R.string.title),
            modifier = Modifier
                .width(dimensionResource(R.dimen.dp_200))
                .padding(start = dimensionResource(R.dimen.dp_10))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        NotifyTextField(
            value = message.value,
            onValueChanged = { message ->
                notificationViewModel.updateMessage(message)
            },
            placeHolderName = stringResource(R.string.message),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_300))
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        Button(
            onClick = {
                scope.launch {
                    notificationViewModel.sendNotify()
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
                text = stringResource(R.string.send),
                style = MaterialTheme.typography.labelMedium
            )
        }
        OutlinedButton(
            onClick = {
                notificationViewModel.clean()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun NotifyTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeHolderName: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            Text(
                text = placeHolderName,
                style = MaterialTheme.typography.labelLarge
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}