package bi.vovota.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.CartItem
import bi.vovota.madeinburundi.data.model.Company
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.repository.CompanyRepository
import bi.vovota.madeinburundi.data.repository.OrderRepository
import bi.vovota.madeinburundi.data.repository.ProductRepository
import bi.vovota.madeinburundi.data.repository.UnAuthorizedException
import bi.vovota.madeinburundi.data.repository.UserRepository
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
    get() = cartItems.sumOf { it.product.bdiPrice.toDouble() * it.quantity.value }

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
        val user = userRepository.getProfile()
        val total = calculateGrandTotal(user)
        val success = orderRepository.placeOrderMessage(user, cartItems)
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

  fun calculateGrandTotal(user: User?): Double {
    return cartItems.sumOf { item->
      val price = getCartPrice(user, item)
      price * item.quantity.value
    }
  }
  fun getCartPrice(user: User?, item: CartItem): Double {
    return when (user?.code) {
      "254" -> item.product.kePrice
      "255" -> item.product.tzPrice
      "250" -> item.product.rwPrice
      "243" -> item.product.drcPrice
      "256" -> item.product.ugPrice
      else ->  item.product.bdiPrice
    }.toDoubleOrNull() ?: 0.0
  }
}
