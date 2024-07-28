package com.example.jennyserviceapp.ui.screens.orders

import androidx.lifecycle.ViewModel
import com.example.jennyserviceapp.data.model.Checkout
import com.example.jennyserviceapp.data.model.UserAddress
import com.example.jennyserviceapp.data.model.UserInformation
import com.example.jennyserviceapp.data.repository.Repository
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
    val checkoutDetails: Boolean = false
)

class CustomerOrdersViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _userInfo = MutableStateFlow(UserInfo())
    val uiState: StateFlow<UserInfo> = _userInfo

    val getCheckout: Flow<List<Checkout>> = repository.getCheckoutList

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
}