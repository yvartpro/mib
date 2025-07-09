package com.example.madeinburundi.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.madeinburundi.ui.section.CategoryRow
import com.example.madeinburundi.ui.section.ProductCard
import com.example.madeinburundi.ui.section.ProductList
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.CategoryViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel

@Composable
fun SearchScreen(
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  categoryName: String? = null,
  navController: NavController,
  categoryViewModel: CategoryViewModel
) {
  val searchQuery = productViewModel.searchQuery
  val products = if(categoryName != null) {
    productViewModel.products.filter {
      it.category.equals(categoryName, ignoreCase = true)
    }
  } else if(categoryName == "tous"){
    productViewModel.products
  } else {
    productViewModel.filteredProducts
  }

  Column(modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .padding(16.dp)) {
    if (categoryName != null) {
      CategoryRow(navController = navController, categoryViewModel)
    } else {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { productViewModel.onSearch(it) },
        placeholder = { Text("Recherchez un produit...") },
        modifier = Modifier.fillMaxWidth()
      )
    }
    Spacer(modifier = Modifier.height(16.dp))

    if (products.isEmpty()) {
      Text("Aucun produit.")
    } else {
      ProductList(cartViewModel = cartViewModel,productViewModel = productViewModel, navController = navController)
    }
  }
}
