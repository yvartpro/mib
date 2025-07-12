package com.example.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.NewOrder
import com.example.madeinburundi.data.model.Order
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.data.repository.OrderRepository
import com.example.madeinburundi.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
  private val orderRepo: OrderRepository,
  private val userRepository: UserRepository

): ViewModel() {

  private val _result = MutableStateFlow("")
  val result: StateFlow<String> = _result
  var orders by mutableStateOf<List<Order>>(emptyList())
    private set

  var emptyMsg by mutableStateOf("")
    private set

  var isLoading by mutableStateOf(false)
    private set

  fun placeOrder(customerId: Int, description: String) {
    viewModelScope.launch {
      val user = userRepository.getProfile()
      println("User ordering: $user")
      val success = orderRepo.placeOrder(NewOrder(user.id, description))
      _result.value = if (success) "Commande envoyée" else "Échec de la commande"
    }
  }

  fun loadOrders() {
    viewModelScope.launch {
      isLoading = true
      val result = orderRepo.getOrders()
      orders = result
      isLoading  = false
    }
  }

}
