package com.example.jennyserviceapp.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.ServiceViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun AccountsScreen(
    restartApp: (ServiceDestinations) -> Unit,
    accountViewModel: AccountViewModel = viewModel(factory = ServiceViewModelProvider.Factory)
) {

    LaunchedEffect(key1 = true) {
        accountViewModel.initialize(restartApp)
    }
    val photoUri = accountViewModel.photoUri.collectAsState()
    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        accountViewModel.photoUri.value = it
    }

    val displayName = accountViewModel.displayName.collectAsState()
    val userInfo = accountViewModel.userInfo
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_300))
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.dp_120))
                ) {
                    AccountTextField(
                        value = displayName.value,
                        onValueChanged = { displayName ->
                            accountViewModel.updateDisplayName(displayName)
                        },
                        placeHolderName = userInfo?.displayName ?: stringResource(R.string.name),
                        enable = false
                    )
                    userInfo?.email?.let {
                        Text(
                            text = it
                        )
                    }
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userInfo?.photoUri ?: photoUri.value)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(dimensionResource(R.dimen.dp_150))
                        .clickable {
                            selectImage.launch("image/*")
                        }
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        accountViewModel.save()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        end = dimensionResource(R.dimen.dp_10)
                    )
            ) {
                Text(text = "save")
            }
        }
    }
}


@Composable
fun AccountTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeHolderName: String,
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            Text(
                text = placeHolderName,
                style = MaterialTheme.typography.labelMedium
            )
        },
        readOnly = enable,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_20)
            )
    )
}