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
import com.example.madeinburundi.data.repository.UnAuthorizedException
import com.example.madeinburundi.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
  private val productRepository: ProductRepository,
  private val companyRepository: CompanyRepository,
  private val orderRepository: OrderRepository,
  private val userRepository: UserRepository
) : ViewModel() {

  var products by mutableStateOf<List<Product>>(emptyList())
    private set

  var isAdded by mutableStateOf(false)
    private set

  var notif by mutableStateOf("")
    private set

  fun notifyAdded(msg: String) {
    isAdded = true
    notif  = msg
  }

  fun resetIsAdded() {
    isAdded = false
  }

  var companies by mutableStateOf<List<Company>>(emptyList())
    private set

  var cartItems = mutableStateListOf<CartItem>()
    private set

  var isCheckingOut by mutableStateOf(false)
    private set

  var grandTotal: Double = 0.0
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
    notifyAdded("Produit ajouté")
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

  fun checkoutAndClear(onResult: (Boolean) -> Unit) {
    isCheckingOut = true
    viewModelScope.launch {
      try {
        val user = userRepository.getProfile() // Might throw
        val success = orderRepository.placeOrderMessage(user.id, cartItems)
        if (success) {
          cartItems.clear()
          grandTotal = 0.0
        } else {
          notifyAdded("Commande échouée")
        }
        onResult(success)
      } catch (e: UnAuthorizedException) {
        notifyAdded("Vous devez vous connecter")
        onResult(false)
      } catch (e: Exception) {
        notifyAdded("Erreur: ${e.message}")
        onResult(false)
      } finally {
        isCheckingOut = false
      }
    }
  }




}
