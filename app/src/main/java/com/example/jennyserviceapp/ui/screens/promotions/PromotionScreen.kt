package com.example.jennyserviceapp.ui.screens.promotions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceViewModelProvider
import com.example.jennyserviceapp.data.model.Promotions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun PromotionScreen(
    modifier: Modifier = Modifier,
    viewModel: PromotionsViewModel = viewModel(factory = ServiceViewModelProvider.Factory)
) {

    val uiState = viewModel.uiState.collectAsState()
    val name = viewModel.name.collectAsState()
    val itemWorth = viewModel.itemWorth.collectAsState()
    val discount = viewModel.discount.collectAsState()
    val valid = viewModel.valid.collectAsState()
    val image = viewModel.image.collectAsState()
    val scope = rememberCoroutineScope()

    val promotions = viewModel.promotion.collectAsState(initial = null)

    val selectImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        viewModel.image.value = it
        viewModel.savePromotionImage(it)
    }

    Scaffold(
        floatingActionButton = {
            IconButton(
                onClick = {
                    viewModel.onImageClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.addImage)
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
        ) {
            if (uiState.value.isHome) {
                if (promotions.value != null) {
                   promotions.value?.let { promotions ->
                       PromotionCardList(
                           promotions = promotions,
                           onDeleteClicked = {
                               scope.launch {
                                   viewModel.deletePromotion(it)
                               }
                           }
                       )
                   }
                } else {
                    Text(text = "Welcome to Promotion Screen")
                }
            } else {
                PromotionAdd(
                    name = name.value,
                    onNameChanged = { name ->
                        viewModel.onNameChanged(name)
                    },
                    itemWorth = itemWorth.value,
                    onItemWorthChanged = { worth ->
                        viewModel.onItemWorthChanged(worth)
                    },
                    discount = discount.value,
                    onDiscountChanged = { discount ->
                        viewModel.onDiscountChanged(discount)
                    },
                    valid = valid.value,
                    onValidChanged = { validDays ->
                        viewModel.onValidChanged(validDays)
                    },
                    image = image.value.toString(),
                    onImageClicked = {
                        selectImage.launch("image/*")
                    },
                    onSaveClicked = {
                        scope.launch {
                            viewModel.savePromotion()
                        }
                        viewModel.isHome()
                    },
                    onCancelClicked = {
                        viewModel.isHome()
                    }
                )
            }
        }
    }
}


@Composable
fun PromotionAdd(
    name: String,
    onNameChanged: (String) -> Unit,
    itemWorth: String,
    onItemWorthChanged: (String) -> Unit,
    discount: String,
    onDiscountChanged: (String) -> Unit,
    valid: String,
    onValidChanged: (String) -> Unit,
    image: String?,
    onImageClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_500))
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Row(
            modifier = modifier
                .height(dimensionResource(R.dimen.dp_350))
        ) {
            PromotionText(
                name,
                onNameChanged,
                itemWorth,
                onItemWorthChanged,
                discount,
                onDiscountChanged,
                valid,
                onValidChanged,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_200))
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = image,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.profileimage),
                modifier = Modifier
                    .clickable { onImageClicked() }
            )
        }
        Button(
            onClick = onSaveClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(text = stringResource(R.string.save))
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_3)))
        OutlinedButton(
            onClick = onCancelClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(text = stringResource(R.string.cancel))
        }
    }
}

@Composable
fun PromotionCardList(
    promotions: List<Promotions>,
    onDeleteClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(promotions, key = { promotion -> promotion.id }) { promotion ->
            PromotionsCard(
                promotion = promotion,
                onDeleteClicked = onDeleteClicked
            )
        }
    }
}

@Composable
fun PromotionsCard(
    promotion: Promotions,
    onDeleteClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_3)),
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_300))
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Row(
            modifier = modifier

        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.dp_10))
            ) {
                Text(
                    text = promotion.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
                Text(
                    text = stringResource(R.string.buy) +" "+ promotion.worth +" "+ stringResource(R.string.worth),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_3)))
                Text(
                    text = stringResource(R.string.get) +" "+ promotion.discount + stringResource(R.string.discountOff),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_3)))
                Text(
                    text = stringResource(R.string.valid) +" "+ promotion.validDays + stringResource(R.string.days),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_120)))
                IconButton(
                    onClick = {
                        onDeleteClicked(promotion.id)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                    )
                }
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(promotion.image)
                    .crossfade(true)
                    .build(),
                contentDescription = promotion.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.ic_connection_error),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_200))
                    .height(dimensionResource(R.dimen.dp_300))
            )
        }
    }
}

@Composable
fun PromotionText(
    name: String,
    onNameChanged: (String) -> Unit,
    itemWorth: String,
    onItemWorthChanged: (String) -> Unit,
    discount: String,
    onDiscountChanged: (String) -> Unit,
    valid: String,
    onValidChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        PromotionTextField(
            value = name,
            onValueChanged = onNameChanged,
            placeholder = {
                Text(
                    text = stringResource(R.string.name),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
        )
        Row {
            Text(
                text = stringResource(R.string.buy),
                style = MaterialTheme.typography.labelLarge
            )
            PromotionTextField(
                value = itemWorth,
                onValueChanged = onItemWorthChanged,
                placeholder = {
                    Text(
                        text = stringResource(R.string.amount),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_90))
            )
            Text(
                text = stringResource(R.string.worth),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Row {
            Text(
                text = stringResource(R.string.get),
                style = MaterialTheme.typography.labelLarge
            )
            PromotionTextField(
                value = discount,
                onValueChanged = onDiscountChanged,
                placeholder = {
                    Text(
                        text = stringResource(R.string.discount),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_90))
            )
            Text(
                text = stringResource(R.string.discountOff),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Row {
            Text(
                text = stringResource(R.string.valid),
                style = MaterialTheme.typography.labelLarge
            )
            PromotionTextField(
                value = valid,
                onValueChanged = onValidChanged,
                placeholder = {
                    Text(
                        text = stringResource(R.string.num),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_90))
            )
            Text(
                text = stringResource(R.string.days),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun PromotionTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}