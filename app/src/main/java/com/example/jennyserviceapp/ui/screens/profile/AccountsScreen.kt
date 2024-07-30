package com.example.jennyserviceapp.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.ServiceViewModelProvider
import com.example.jennyserviceapp.data.model.JennyAccountInfo
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
    val bankInfo = accountViewModel.bankInfo.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        AccountBankInfo(
            accountViewModel = accountViewModel,
            onClicked = {
                scope.launch {
                    accountViewModel.accountInfo()
                }
            },
            jennyAccountInfo = bankInfo.value
        )
        Spacer(modifier = Modifier .height(dimensionResource(R.dimen.dp_50)))
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
            Row(modifier = Modifier.padding(dimensionResource(R.dimen.dp_10))) {
                Column(
                    modifier = Modifier

                ) {
                    Row {
                        Text(
                            text = stringResource(R.string.name),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(
                                    end = dimensionResource(R.dimen.dp_5),
                                    start = dimensionResource(R.dimen.dp_10),
                                    top = dimensionResource(R.dimen.dp_15)
                                )
                        )
                        AccountTextField(
                            value = displayName.value,
                            onValueChanged = { displayName ->
                                accountViewModel.updateDisplayName(displayName)
                            },
                            placeHolderName = userInfo?.displayName ?: stringResource(R.string.name),
                            modifier = Modifier
                                .padding(
                                    bottom = dimensionResource(R.dimen.dp_10)
                                )
                        )
                    }
                    Row {
                        userInfo?.email?.let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .padding(
                                        top = dimensionResource(R.dimen.dp_40),
                                        start = dimensionResource(R.dimen.dp_10),
                                        end = dimensionResource(R.dimen.dp_5)
                                    )
                            )
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(userInfo?.photoUri ?: photoUri.value)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            placeholder = painterResource(R.drawable.profileimage),
                            error = painterResource(R.drawable.profileimage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(dimensionResource(R.dimen.dp_100))
                                .clickable {
                                    selectImage.launch("image/*")
                                }
                        )
                    }
                }
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
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun AccountBankInfo(
    accountViewModel: AccountViewModel,
    jennyAccountInfo: JennyAccountInfo?,
    onClicked: () -> Unit
) {
    val firstName = accountViewModel.firstName.collectAsState()
    val middleName = accountViewModel.middleName.collectAsState()
    val lastName = accountViewModel.lastName.collectAsState()
    val bankName = accountViewModel.bankName.collectAsState()
    val bankAccount = accountViewModel.bankAccount.collectAsState()
    val phoneNumber = accountViewModel.phoneNumber.collectAsState()
    Column {
        Text(
            text = stringResource(R.string.bankAccount),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        AccountTextField(
            value = jennyAccountInfo?.firstName ?: firstName.value,
            onValueChanged = { firstName ->
                accountViewModel.updateFirstName(firstName)
            },
            placeHolderName = stringResource(R.string.firstName),
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.dp_10))
        )
        AccountTextField(
            value = jennyAccountInfo?.middleName ?: middleName.value,
            onValueChanged = { middleName ->
                accountViewModel.updateMiddle(middleName)
            },
            placeHolderName = stringResource(R.string.middleName),
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.dp_10))
        )
        AccountTextField(
            value = jennyAccountInfo?.lastName ?: lastName.value,
            onValueChanged = { lastName ->
                accountViewModel.updateLastName(lastName)
            },
            placeHolderName = stringResource(R.string.lastName),
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.dp_10))
        )
        Row {
            AccountTextField(
                value = jennyAccountInfo?.bankName ?: bankName.value,
                onValueChanged = { bankName ->
                    accountViewModel.updateBankName(bankName)
                },
                placeHolderName = stringResource(R.string.bankName),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_120))
            )
            AccountTextField(
                value = jennyAccountInfo?.bankAccount ?: bankAccount.value,
                onValueChanged = { account ->
                    accountViewModel.updateBankAccount(account)
                },
                placeHolderName = stringResource(R.string.bankAccount),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_10))
            )
        }
        AccountTextField(
            value = jennyAccountInfo?.phoneNumber ?: phoneNumber.value,
            onValueChanged = { number ->
                accountViewModel.updatePhoneNumber(number)
            },
            placeHolderName = stringResource(R.string.phoneNumber)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Button(
            onClick = onClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}


@Composable
fun AccountTextField(
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
                style = MaterialTheme.typography.labelMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_20)
            )
    )
}