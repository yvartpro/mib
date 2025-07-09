package com.example.madeinburundi.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.madeinburundi.ui.screen.AuthScreen
import com.example.madeinburundi.ui.screen.CartScreen
import com.example.madeinburundi.ui.screen.CompanyDetailsScreen
import com.example.madeinburundi.ui.screen.CompanyScreen
import com.example.madeinburundi.ui.screen.HomeScreen
import com.example.madeinburundi.ui.screen.ProductScreen
import com.example.madeinburundi.ui.screen.ProfileScreen
import com.example.madeinburundi.ui.screen.SearchScreen
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.CategoryViewModel
import com.example.madeinburundi.viewmodel.CompanyViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel
import com.example.madeinburundi.viewmodel.UserViewModel
import okhttp3.Route

@Composable
fun AppNavGraph(
  modifier: Modifier,
  navController: NavHostController,
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  userViewModel: UserViewModel,
  categoryViewModel: CategoryViewModel,
  companyViewModel: CompanyViewModel
) {
  LaunchedEffect(Unit) {
    productViewModel.loadProducts()
    cartViewModel.companies
    companyViewModel.loadCompanies()
  }

  NavHost(navController, startDestination = NavDestinations.HOME, modifier = modifier) {
    composable(NavDestinations.HOME) {
      HomeScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        onNavigateToCart = { navController.navigate(NavDestinations.CART) },
        categoryViewModel = categoryViewModel,
        navController = navController
      )
    }
    composable(NavDestinations.CART) {
      CartScreen(
        cartViewModel = cartViewModel,
        userViewModel = userViewModel,
        navController = navController,
        onBack = { navController.popBackStack() }
      )
    }
    composable(NavDestinations.COMPANIES)   {
      CompanyScreen(
        navController = navController,
        companies = companyViewModel.companies,
        companyViewModel = companyViewModel
      )
    }
    composable(NavDestinations.PRODUCT) { backStackEntry->
      val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
      val product = productViewModel.products.find { it.id == productId }
      product?.let {
        ProductScreen(
          product = it,
          cartViewModel = cartViewModel,
          productViewModel = productViewModel,
          navController = navController
        )
      }
    }
    composable(NavDestinations.PROFILE) {
      ProfileScreen( navController = navController, userViewModel = userViewModel)
    }
    composable(NavDestinations.AUTH) {
      AuthScreen(
        modifier = Modifier,
        onBackClick = { navController.navigate(NavDestinations.HOME)},
        onLoginSuccess = { navController.navigate(NavDestinations.PROFILE)},
        navController = navController,
        userViewModel = userViewModel
      )
    }
    composable(NavDestinations.SEARCH) {
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        navController = navController,
        categoryViewModel = categoryViewModel
      )
    }
    composable(NavDestinations.CATEGORY) { backStackEntry->
      val categoryName = backStackEntry.arguments?.getString("category")
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        categoryName = categoryName.takeIf { it != null },
        navController = navController,
        categoryViewModel = categoryViewModel
      )
    }
    composable(NavDestinations.COMPANY) { backStackEntry ->
      val companyId = backStackEntry.arguments?.getString("companyId")?.toIntOrNull()
      if (companyId != null) {
        CompanyDetailsScreen(
          companyId = companyId,
          cartViewModel = cartViewModel,
          navController = navController,
          productViewModel = productViewModel,
          companies = companyViewModel.companies
        )
      }
    }
  }
}

