package com.example.jennyserviceapp.ui.screens.promotions

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jennyserviceapp.data.model.Promotions
import com.example.jennyserviceapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PromotionsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PromotionUiState())
    val uiState: StateFlow<PromotionUiState> = _uiState

    var name = MutableStateFlow("")
    var itemWorth = MutableStateFlow("")
    var discount = MutableStateFlow("")
    var valid = MutableStateFlow("")
    var validBoolean = mutableStateOf(false)

    val image = MutableStateFlow<Uri?>(null)

    val promotion = repository.getPromotions()

    fun savePromotionImage(image: Uri?) {
        viewModelScope.launch {
            repository.saveImageToStorage(image)
        }
    }

    suspend fun savePromotion() {
        CoroutineScope(Dispatchers.IO).launch {
            val promotion = Promotions(
                name = name.value,
                worth = itemWorth.value,
                discount = discount.value,
                validDays = valid.value,
            )
            runCatching {
                repository.addPromotions(promotion)
            }
            clean()
        }
    }

    suspend fun deletePromotion(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deletePromotions(id)
        }
    }

    fun isHome() {
        _uiState.update {
            it.copy(
                isHome = true
            )
        }
    }

    private fun clean() {
        this.name.value = ""
        this.itemWorth.value = ""
        this.discount.value = ""
        this.valid.value = ""
        this.image.value = null
    }

    fun onNameChanged(name: String) {
        this.name.value = name
    }

    fun onItemWorthChanged(worth: String) {
        this.itemWorth.value = worth
    }

    fun onDiscountChanged(discount: String) {
        this.discount.value = discount
    }

    fun onValidChanged(valid: String) {
        this.valid.value = valid
    }

    fun onImageClicked() {
        _uiState.update {
            it.copy(isHome = false)
        }
    }
}