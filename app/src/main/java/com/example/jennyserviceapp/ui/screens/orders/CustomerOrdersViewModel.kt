package com.example.jennyserviceapp.ui.screens.orders

import androidx.lifecycle.ViewModel
import com.example.jennyserviceapp.data.model.Checkout
import com.example.jennyserviceapp.data.model.CheckoutCancel
import com.example.jennyserviceapp.data.model.UserAddress
import com.example.jennyserviceapp.data.model.UserInformation
import com.example.jennyserviceapp.data.repository.Repository
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserInfo(
    val userinfo: UserInformation? = null,
    val userAddress: UserAddress? = null,
    val checkout: Checkout? = null,
    val checkoutDetails: Boolean = false,
    val cancelAlert: Boolean = false,
    val delivered: Boolean = false
)

class CustomerOrdersViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _userInfo = MutableStateFlow(UserInfo())
    val uiState: StateFlow<UserInfo> = _userInfo

    val getCheckout: Flow<List<Checkout>> = repository.getCheckoutList

    val reason = MutableStateFlow("")

    suspend fun getUserInfoAndCheckoutById(id: String, userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
           runCatching {
               _userInfo.update {
                   it.copy(
                       checkout = repository.getCheckoutById(id),
                       userinfo = repository.getUserName(userId),
                       userAddress = repository.getUserAddress(userId)
                   )
               }
               _userInfo.update {
                   it.copy(
                       checkoutDetails = true
                   )
               }
           }
        }
    }

    fun onDelivered() {
        _userInfo.update {
            it.copy(
                delivered = true
            )
        }
    }

    suspend fun cancelOrder(checkout: Checkout) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val checkoutCanceled = CheckoutCancel(
                    userId = checkout.userId,
                    itemName = checkout.itemName,
                    itemImage = checkout.itemImage,
                    dateCreated = Timestamp.now(),
                    reason = reason.value
                )
                repository.addToUserCancelCheckout(checkoutCanceled)
                repository.deleteCheckout(checkout)
            }
        }
    }

    suspend fun updatePending(checkout: Checkout) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val checkoutOrder = checkout.copy(
                    orderPending = false,
                )
                repository.updatePending(checkoutOrder)
            }
        }
    }
    suspend fun updateDelivered(checkout: Checkout) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val checkoutOrder = checkout.copy(
                    orderReceived = true,
                )
                repository.updateDelivered(checkoutOrder)
            }
        }
    }

    fun onReasonChanged(reason: String) {
        this.reason.value = reason
    }

    fun cancelAlert() {
        _userInfo.update {
            it.copy(
                cancelAlert = false
            )
        }
    }
    fun onCancelAlert() {
        _userInfo.update {
            it.copy(
                cancelAlert = true
            )
        }
    }

    fun back() {
        _userInfo.update {
            it.copy(
                checkoutDetails = false
            )
        }
    }
}