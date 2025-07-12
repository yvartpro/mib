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
import com.example.madeinburundi.viewmodel.CategoryViewModel
import com.example.madeinburundi.viewmodel.CompanyViewModel
import com.example.madeinburundi.viewmodel.OrderViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel
import com.example.madeinburundi.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun EcommerceApp(
  cartViewModel: CartViewModel = hiltViewModel(),
  productViewModel: ProductViewModel = hiltViewModel(),
  companyViewModel: CompanyViewModel = hiltViewModel(),
  userViewModel: UserViewModel = hiltViewModel(),
  categoryViewModel: CategoryViewModel = hiltViewModel(),
  orderViewModel: OrderViewModel = hiltViewModel()
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val isAdded = cartViewModel.isAdded
  val msg = cartViewModel.notif
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
        message = msg,
        topPadding = if ( currentRoute == NavDestinations.HOME) 100.dp else 24.dp
      )
    },
    topBar = {
      if (currentRoute != NavDestinations.AUTH) {
        TopBar(navController = navController, userViewModel = userViewModel)
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
      userViewModel = userViewModel,
      categoryViewModel = categoryViewModel,
      orderViewModel = orderViewModel
    )
  }
}
