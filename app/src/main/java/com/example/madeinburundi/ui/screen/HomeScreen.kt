package com.example.madeinburundi.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.createGraph
import coil.compose.AsyncImage
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.ui.nav.NavDestinations
import com.example.madeinburundi.ui.section.CategoryRow
import com.example.madeinburundi.ui.section.ProductImageRow
import com.example.madeinburundi.ui.section.ProductList
import com.example.madeinburundi.ui.section.Recommended
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.CategoryViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  categoryViewModel: CategoryViewModel,
  onNavigateToCart: () -> Unit,
  navController: NavController
) {
  LaunchedEffect(Unit) {
    productViewModel.loadProducts()
    println(productViewModel.products)
  }
  val products = productViewModel.products
  val isLoading = productViewModel.isLoading

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(vertical = 8.dp)
  ) {
    HomeSection(title = "Catégories") {
      CategoryRow(navController = navController, viewModel = categoryViewModel)
    }
    HomeSection(title = "Les plus récents") {
      ProductImageRow(products = productViewModel.products, navController = navController)
    }
    HomeSection(title = "Recommendés") {
      Recommended(products = productViewModel.products, navController = navController)
    }
    HomeSection(title = "Produits") {
      ProductList(cartViewModel = cartViewModel, products = products, navController = navController)
    }
  }
}


@Composable
fun HomeSection(
  title: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Column {
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 14.sp,
      modifier = Modifier.padding(2.dp)
    )
    content()
    Spacer(Modifier.height(16.dp))
  }
}
