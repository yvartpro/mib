package bi.vovota.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
  private val productRepository: ProductRepository
) : ViewModel() {

  var products by mutableStateOf<List<Product>>(emptyList())
    private set

  var filteredProducts by mutableStateOf<List<Product>>(emptyList())
    private set

  var emptyMsg by mutableStateOf("")
    private set

  var searchQuery by mutableStateOf("")

  var isLoading by mutableStateOf(false)
    private set

  fun loadProducts() {
    viewModelScope.launch {
      isLoading = true
      val result = productRepository.getProducts()
      if (result.isNotEmpty())  {
        products = result
        isLoading = false
      }
    }
  }

  fun onSearch(query: String) {
    searchQuery = query
    filteredProducts = if (query.isBlank()) {
      products
    } else {
      products.filter {
        it.name.contains(query, ignoreCase = true)
      }
    }
  }

  fun getPrice(product: Product, user: User?): String {
    return when (user?.code) {
      "254" -> product.kePrice
      "255" -> product.tzPrice
      "250" -> product.rwPrice
      "243" -> product.drcPrice
      "256" -> product.ugPrice
      else -> product.bdiPrice
    }
  }

  fun getCurrency(user: User?): String {
      return when (user?.code) {
        "254" -> "KSH"
        "255" -> "TSH"
        "250" -> "RWF"
        "243" -> "FC"
        "256" -> "UGX"
        else -> "FBU"
      }
  }
}

