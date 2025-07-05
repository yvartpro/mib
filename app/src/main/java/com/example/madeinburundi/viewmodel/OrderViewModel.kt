package com.example.madeinburundi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.Order
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

  fun placeOrder(customerId: Int, description: String) {
    viewModelScope.launch {
      val user = userRepository.getProfile()
      println("User ordering: $user")
      val success = orderRepo.placeOrder(Order(user.id, description))
      _result.value = if (success) "Commande envoyée" else "Échec de la commande"
    }
  }
}
