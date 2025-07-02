package com.example.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.CartItem
import com.example.madeinburundi.data.model.Company
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.data.repository.CompanyRepository
import com.example.madeinburundi.data.repository.OrderRepository
import com.example.madeinburundi.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val companyRepository: CompanyRepository,
  private val orderRepository: OrderRepository
) : ViewModel() {

  var products by mutableStateOf<List<Product>>(emptyList())
    private set

  var companies by mutableStateOf<List<Company>>(emptyList())
    private set

  var cartItems = mutableStateListOf<CartItem>()
    private set

  var isCheckingOut by mutableStateOf(false)
    private set

  val grandTotal: Double
    get() = cartItems.sumOf { it.product.price.toDouble() * it.quantity.value }

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    viewModelScope.launch {
      products = productRepository.getProducts()
      companies = companyRepository.getCompanies()
    }
  }

  fun addToCart(product: Product) {
    val existing = cartItems.find { it.product.id == product.id }
    if (existing != null) {
      existing.quantity.value++
    } else {
      cartItems.add(CartItem(id = product.id, product = product, quantity = mutableIntStateOf(1)))
    }
  }

  fun increaseQuantity(productId: Int) {
    cartItems.find { it.product.id == productId }?.apply {
      quantity.value++
    }
  }

  fun decreaseQuantity(productId: Int) {
    cartItems.find { it.product.id == productId }?.let {
      if (it.quantity.value > 1) it.quantity.value-- else cartItems.remove(it)
    }
  }

  fun removeItem(productId: Int) {
    cartItems.removeAll { it.product.id == productId }
  }

  fun clearCart() {
    cartItems.clear()
  }

  fun checkoutAndClear(
    customerId: Int,
    orderRepository: OrderRepository,
    onResult: (Boolean) -> Unit
  ) {
    viewModelScope.launch {
      isCheckingOut = true
      val result = orderRepository.placeOrderMessage(customerId, cartItems)
      if (result) clearCart()
      isCheckingOut = false
      onResult(result)
    }
  }


}
