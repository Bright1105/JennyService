package com.example.jennyserviceapp.ui.screens.uploadproducts

import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jennyserviceapp.R
import com.example.jennyserviceapp.ServiceDestinations
import com.example.jennyserviceapp.ServiceViewModelProvider
import com.example.jennyserviceapp.UploadFeed
import com.example.jennyserviceapp.data.model.Feeds
import java.util.Locale

@Composable
fun ProductScreen(
    viewModel: ProductsViewModel = viewModel(factory = ServiceViewModelProvider.Factory),
) {
    ProductUpload(
        viewModel = viewModel,
        modifier = Modifier
    )
}


@Composable
private fun ProductUpload(
    viewModel: ProductsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        ProductsItem(
            title = viewModel.title.value,
            onTitleChanged = { viewModel.onTitle(it)},
            price = viewModel.price.value,
            onPriceChanged = { viewModel.onPrice(it)},
            brand = viewModel.brand.value,
            onBrandChanged = { viewModel.onBrand(it)},
            description = viewModel.description.value,
            onDescriptionChanged = { viewModel.onDescription(it)},
            itemType = viewModel.itemType.value,
            onItemTypeChanged = { viewModel.onItemType(it)},
            itemAvailable = viewModel.itemAvailable.value,
            onItemAvailableChanged = { viewModel.onAvailableItem(it) },
            productsViewModel = viewModel
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
        Button(
            onClick = {
                viewModel.addProduct()
            },
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10)),
        ) {
            Text(
                text = stringResource(R.string.save).replaceFirstChar {
                    it.uppercaseChar()
                },
                style = MaterialTheme.typography.labelLarge
            )
        }
        Button(onClick = {
            viewModel.cleanInput()
        },
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}



@Composable
private fun ProductsItem(
    title: String,
    onTitleChanged: (String) -> Unit,
    price: String,
    onPriceChanged: (String) -> Unit,
    brand: String,
    onBrandChanged: (String) -> Unit,
    itemAvailable: String,
    onItemAvailableChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    itemType: String,
    onItemTypeChanged: (String) -> Unit,
    productsViewModel: ProductsViewModel,
    modifier: Modifier = Modifier
) {

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) {
        productsViewModel.addImageToStorage(it)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        ProductItemField(
            value = title,
            onValueChanged = onTitleChanged,
            label = {
                    Text(
                        text = stringResource(R.string.name),
                        style = MaterialTheme.typography.labelLarge
                    )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
        ProductItemField(
            value = price,
            onValueChanged = onPriceChanged,
            label = {
                Text(
                    text = stringResource(R.string.price),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Button(
            onClick = { selectImageLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_65))
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.selectImage),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Row(
            modifier = modifier
        ) {
            ProductItemField(
                value = brand,
                onValueChanged = onBrandChanged,
                label = {
                    Text(
                        text = stringResource(R.string.brand),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .weight(1f)

            )
            ProductItemField(
                value = itemAvailable,
                onValueChanged = onItemAvailableChanged,
                label = {
                    Text(
                        text = stringResource(R.string.noItem),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.dp_120))
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        ProductItemField(
            value = description,
            onValueChanged = onDescriptionChanged,
            label = {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
        ProductItemField(
            value = itemType,
            onValueChanged = onItemTypeChanged,
            label = {
                Text(
                    text = stringResource(R.string.itemType),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )
    }
}





@Composable
fun ProductItemField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: @Composable() ( () -> Unit),
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = label,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_70))
            .padding(10.dp)
    )
}