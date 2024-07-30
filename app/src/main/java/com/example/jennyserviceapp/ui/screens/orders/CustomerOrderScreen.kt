package com.example.jennyserviceapp.ui.screens.orders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceViewModelProvider
import com.example.jennyserviceapp.data.model.Checkout
import com.example.jennyserviceapp.data.model.UserAddress
import com.example.jennyserviceapp.data.model.UserInformation
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomerOrdersScreen(
    ordersViewModel: CustomerOrdersViewModel = viewModel(factory = ServiceViewModelProvider.Factory),
    popUp: () -> Unit
) {
    val uiState = ordersViewModel.uiState.collectAsState()
    val checkouts = ordersViewModel.getCheckout.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val reason = ordersViewModel.reason.collectAsState()
    val pagerState = rememberPagerState {
        OrdersTabs.entries.size
    }
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if (uiState.value.checkoutDetails) {
            uiState.value.checkout?.let {
                uiState.value.userinfo?.let { it1 ->
                    uiState.value.userAddress?.let { it2 ->
                        OrdersDetails(
                            checkout = it,
                            userInformation = it1,
                            userAddress = it2,
                            onBackClicked = {
                                ordersViewModel.back()
                            },
                            cancelAlert = uiState.value.cancelAlert,
                            onClicked = {
                                scope.launch {
                                    ordersViewModel.cancelOrder(it)
                                    ordersViewModel.cancelAlert()
                                    ordersViewModel.back()
                                }
                            },
                            onCancelAlert = {
                                ordersViewModel.onCancelAlert()
                            },
                            value = reason.value,
                            onValueChanged = { newReason ->
                                ordersViewModel.onReasonChanged(newReason)
                            },
                            canceledAlert = {
                                ordersViewModel.cancelAlert()
                            },
                            delivered = uiState.value.delivered,
                            acceptOrder = {
                                scope.launch {
                                    ordersViewModel.updatePending(it)
                                    ordersViewModel.onDelivered()
                                    ordersViewModel.back()
                                }
                            },
                            onDelivered = {
                                scope.launch {
                                    ordersViewModel.updateDelivered(it)
                                    ordersViewModel.back()
                                }
                            }
                        )
                    }
                }
            }
        } else {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                OrdersTabs.entries.forEachIndexed { index, ordersTabs ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            scope.launch {
                                selectedTabIndex =  index
                            }
                        },
                        text = {
                            Text(
                                text = ordersTabs.text,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> {
                        OrdersContentList(
                            checkouts = checkouts.value.filter {
                                !it.orderReceived
                            }.sortedByDescending {
                                it.userId
                            },
                            onClicked = { id, userId ->
                                scope.launch {
                                    ordersViewModel.getUserInfoAndCheckoutById(id, userId)
                                }
                            }
                        )
                    }
                    1 -> {
                        OrdersContentList(
                            checkouts = checkouts.value.filter {
                                it.orderReceived
                            }.sortedByDescending {
                                it.dateCreated
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersContentList(
    modifier: Modifier = Modifier,
    checkouts: List<Checkout>,
    onClicked: ((String, String) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(checkouts, key = { checkout -> checkout.id }) {
            OrdersContent(
                checkout = it,
                modifier = Modifier
                    .clickable {
                        if (onClicked != null) {
                            onClicked(it.id, it.userId)
                        }
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersDetails(
    modifier: Modifier = Modifier,
    checkout: Checkout,
    userInformation: UserInformation,
    onBackClicked: () -> Unit,
    userAddress: UserAddress,
    cancelAlert: Boolean,
    onCancelAlert : () -> Unit,
    canceledAlert: () -> Unit,
    onClicked: (Checkout) -> Unit,
    acceptOrder: (Checkout) -> Unit,
    onDelivered: (Checkout) -> Unit,
    delivered: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,

) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        IconButton(
            onClick = onBackClicked
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5)))
        OrdersContent(
            checkout = checkout
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            OrdersDetailButton(
                onClicked = onCancelAlert,
                text = stringResource(R.string.cancel),
                buttonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.dp_15))

            )
            OrdersDetailButton(
                onClicked = { if (!delivered) acceptOrder(checkout) else onDelivered(checkout) },
                text = if (!delivered) stringResource(R.string.accept) else stringResource(R.string.deliver),
                buttonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            )
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        Text(
            text = stringResource(R.string.customer),
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userInformation.image)
                    .crossfade(true)
                    .build(),
                contentDescription = userInformation.firstName,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_connection_error),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_50))
                    .clip(CircleShape)
            )
            Text(
                text = userInformation.firstName + " " + userInformation.lastName,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.dp_8),
                        top = dimensionResource(R.dimen.dp_15)
                    )
            )
        }
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Icon(
                imageVector = Icons.Default.PhoneAndroid,
                contentDescription = stringResource(R.string.phone)
            )
            Text(
                text = userInformation.phoneNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
            )
            userInformation.gender?.let {
                Text(
                    text = it
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Text(
            text = stringResource(R.string.userAddress),
            style = MaterialTheme.typography.titleMedium,
        )
        userAddress.address?.let {
            Text(
                text = it,
            )
        }
        userAddress.additionalInformation?.let {
            Text(
                text = it
            )
        }
        userAddress.region?.let {
            Text(
                text = it
            )
        }
        userAddress.city?.let {
            Text(
                text = it
            )
        }
    }

    if (cancelAlert) {
        BasicAlertDialog(
            onDismissRequest = canceledAlert,
            content = {
                Column {
                    Card(
                        shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                    ) {
                        Text(
                            text = stringResource(R.string.cancelOrder),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(R.dimen.dp_10)
                                )
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                        Text(
                            text = stringResource(R.string.cancelOrderNote),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(R.dimen.dp_10),
                                    end = dimensionResource(R.dimen.dp_10)
                                )
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                        OutlinedTextField(
                            value = value,
                            onValueChange = onValueChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.reason),
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimensionResource(R.dimen.dp_10),
                                    end = dimensionResource(R.dimen.dp_10)
                                )
                                .height(dimensionResource(R.dimen.dp_100))
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                        Row {
                            OrdersDetailButton(
                                onClicked = canceledAlert,
                                text = stringResource(R.string.no),
                                buttonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                modifier = Modifier
                                    .padding(
                                        start = dimensionResource(R.dimen.dp_10),
                                        end = dimensionResource(R.dimen.dp_10)
                                    )
                            )
                            OrdersDetailButton(
                                onClicked = { onClicked(checkout) },
                                text = stringResource(R.string.yes),
                                buttonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        )
    }

}

@Composable
private fun OrdersDetailButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    text: String,
    buttonColors: ButtonColors,
    enable: Boolean = true
) {
    Button(
        onClick = onClicked,
        enabled = enable,
        colors = buttonColors,
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_150))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
private fun OrdersContent(
    modifier: Modifier = Modifier,
    checkout: Checkout,
) {

    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Row(modifier = Modifier.padding(dimensionResource(R.dimen.dp_10))) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(checkout.itemImage)
                    .crossfade(true)
                    .build(),
                contentDescription = checkout.itemName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_100))
                    .padding(end = dimensionResource(R.dimen.dp_5))
            )
            Column {
                Text(
                    text = checkout.itemName,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.dp_180))
                        .height(dimensionResource(R.dimen.dp_45))
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                Text(
                    text = if (checkout.orderPending && !checkout.orderReceived) {
                        stringResource(R.string.pending)
                    } else if (checkout.orderReceived) {
                        stringResource(R.string.complete)
                    } else {
                        stringResource(R.string.ship)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(dimensionResource(R.dimen.dp_3)),
                            color = if (checkout.orderPending && !checkout.orderReceived) MaterialTheme.colorScheme.primaryContainer else Color.Green
                        )
                )
                Text(
                    text = checkout.dateCreated?.toDate().toString(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

enum class OrdersTabs(val text: String) {
    INCOMING(text = "INCOMING"),
    COMPLETED(text = "COMPLETED")
}