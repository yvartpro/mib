package com.example.madeinburundi.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.madeinburundi.R
import com.example.madeinburundi.ui.section.CategoryRow
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
  val products = productViewModel.filteredProducts
    /*if(categoryName != null) {
    productViewModel.products.filter {
      it.category.equals(categoryName, ignoreCase = true)
    }
  } else {
    productViewModel.filteredProducts
  }*/

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
        placeholder = { Text(stringResource(R.string.s_placeholder)) },
        modifier = Modifier.fillMaxWidth()
      )
    }
    Spacer(modifier = Modifier.height(16.dp))

    if (products.isEmpty()) {
      Text(stringResource(R.string.s_no_results))
    } else {
      ProductList(cartViewModel = cartViewModel,productViewModel = productViewModel, navController = navController)
    }
  }
}
