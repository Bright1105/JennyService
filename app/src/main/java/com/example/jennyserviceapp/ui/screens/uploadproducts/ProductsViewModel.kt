package com.example.jennyserviceapp.ui.screens.uploadproducts

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jennyserviceapp.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


sealed class AddItemEvent {
    class Info(val message: String) : AddItemEvent()
    class Error(val message: String, val throwable: Throwable) : AddItemEvent()
}


class ProductsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _addItemEvent: MutableSharedFlow<AddItemEvent> = MutableSharedFlow()
    val addItemEvent: Flow<AddItemEvent>
        get() = _addItemEvent



    var title = mutableStateOf("")
    var price = mutableStateOf("")
    var brand = mutableStateOf("")
    var description = mutableStateOf("")
    var itemType = mutableStateOf("")
    var itemAvailable = mutableStateOf("")


    fun addImageToStorage(imageUri: List<Uri>) {
        viewModelScope.launch {
            repository.addImageToFirebaseStorageAndFireStore(imageUri)
        }
    }

    fun addProduct() {
        viewModelScope.launch {
            repository.addProducts(
                title = title.value,
                brand = brand.value,
                price = price.value.toInt(),
                description = description.value,
                itemType = itemType.value,
                itemAvailable = itemAvailable.value.toInt(),
            )
        }
        cleanInput()
    }

    fun cleanInput() {
        this.title.value = ""
        this.price.value = ""
        this.brand.value = ""
        this.description.value = ""
        this.itemType.value = ""
        this.itemAvailable.value =  ""
    }

    fun onAvailableItem(num: String) {
        this.itemAvailable.value = num
    }
    fun onTitle(title: String) {
        this.title.value = title
    }

    fun onPrice(price: String) {
        this.price.value = price
    }

    fun onBrand(brand: String) {
        this.brand.value = brand
    }

    fun onDescription(des: String) {
        this.description.value = des
    }

    fun onItemType(type: String) {
        this.itemType.value = type
    }


}