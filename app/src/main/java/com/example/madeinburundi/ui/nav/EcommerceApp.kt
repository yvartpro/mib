package com.example.madeinburundi.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.madeinburundi.ui.component.SnackbarTool
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.CompanyViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcommerceApp(
  cartViewModel: CartViewModel = hiltViewModel(),
  productViewModel: ProductViewModel = hiltViewModel(),
  companyViewModel: CompanyViewModel = hiltViewModel()
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val isAdded = cartViewModel.isAdded
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val cartCount: Int by remember {
    derivedStateOf {
      cartViewModel.cartItems.sumOf { it.quantity.value }
    }
  }
  Scaffold(
    snackbarHost = {
      SnackbarTool(
        isVisible = isAdded,
        resetState = { cartViewModel.resetIsAdded()},
        scope = scope,
        snackbarHostState = snackbarHostState,
        message = "Item added",
        topPadding = if ( currentRoute == NavDestinations.HOME) 100.dp else 24.dp
      )
    },
    topBar = {
      if (currentRoute != NavDestinations.CART && currentRoute != NavDestinations.AUTH) {
        TopBar(navController = navController)
      }
    },
    bottomBar = {
      BottomBar(navController = navController, cartItemCount = cartCount)
    }
  ) { innerPadding ->
    AppNavGraph(
      modifier = Modifier.padding(innerPadding),
      navController = navController,
      cartViewModel = cartViewModel,
      productViewModel = productViewModel,
      companyViewModel = companyViewModel,
    )
  }
}
