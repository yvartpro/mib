package com.example.madeinburundi.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.madeinburundi.R
import com.example.madeinburundi.ui.section.CategoryRow
import com.example.madeinburundi.ui.section.ProductImageRow
import com.example.madeinburundi.ui.section.ProductList
import com.example.madeinburundi.ui.section.Recommended
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.CategoryViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel

@Composable
fun HomeScreen(
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  categoryViewModel: CategoryViewModel,
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(vertical = 8.dp, horizontal = 8.dp)
  ) {
    HomeSection(title = stringResource(R.string.category)) {
      CategoryRow(navController = navController, viewModel = categoryViewModel)
    }
    HomeSection(title = stringResource(R.string.latest)) {
      ProductImageRow(productViewModel = productViewModel, navController = navController)
    }
    HomeSection(title = stringResource(R.string.recommended)) {
      Recommended(productViewModel = productViewModel, navController = navController)
    }
    HomeSection(title = stringResource(R.string.products)) {
      ProductList(cartViewModel = cartViewModel, productViewModel = productViewModel, navController = navController)
    }
  }
}


@Composable
fun HomeSection(
  title: String,
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
