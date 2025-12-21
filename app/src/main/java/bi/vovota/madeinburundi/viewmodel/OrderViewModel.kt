package bi.vovota.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.NewOrder
import bi.vovota.madeinburundi.data.model.Order
import bi.vovota.madeinburundi.data.repository.OrderRepo
import bi.vovota.madeinburundi.data.repository.OrderRepository
import bi.vovota.madeinburundi.data.repository.UserRepository
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.utils.UiState
import bi.vovota.madeinburundi.utils.launchWithState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
  private val orderRepo: OrderRepository,
  private val userRepository: UserRepository,
  private val repo: OrderRepo
): ViewModel() {


  private val _result = MutableStateFlow("")

    private val _orders = MutableStateFlow<List<Order>?>(null)
    val orders = _orders.asStateFlow()

  fun placeOrder(customerId: Int, description: String) {
    viewModelScope.launch {
      val user = userRepository.getProfile()
      val success = orderRepo.placeOrder(NewOrder(user.id, description))
      _result.value = if (success) "Commande envoyée" else "Échec de la commande"
    }
  }

  private val _loadOrderState = MutableStateFlow(UiState<List<Order>>())
  val loadOrderState = _loadOrderState.asStateFlow()
  fun loadOrders() {
    launchWithState(
      stateFlow = _loadOrderState,
      block = { repo.getOrders() },
      onSuccess = { _orders.value = it },
      onFailure = {
        Logger.e("Error loading order", it.message ?: "Unknown error")
      }
    )
  }

}
