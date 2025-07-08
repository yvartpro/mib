package com.example.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.data.repository.ProductRepository
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
}

