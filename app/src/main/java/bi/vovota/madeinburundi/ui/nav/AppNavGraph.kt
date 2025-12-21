package bi.vovota.madeinburundi.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bi.vovota.madeinburundi.ui.screen.AuthScreen
import bi.vovota.madeinburundi.ui.screen.CartScreen
import bi.vovota.madeinburundi.ui.screen.CompanyDetailsScreen
import bi.vovota.madeinburundi.ui.screen.CompanyScreen
import bi.vovota.madeinburundi.ui.screen.HomeScreen
import bi.vovota.madeinburundi.ui.screen.ProductScreen
import bi.vovota.madeinburundi.ui.screen.ProfileScreen
import bi.vovota.madeinburundi.ui.screen.SearchScreen
import bi.vovota.madeinburundi.viewmodel.AuthState
import bi.vovota.madeinburundi.viewmodel.AuthViewModel
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import bi.vovota.madeinburundi.viewmodel.CategoryViewModel
import bi.vovota.madeinburundi.viewmodel.CompanyViewModel
import bi.vovota.madeinburundi.viewmodel.OrderViewModel
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
  modifier: Modifier,
  navController: NavHostController,
  authViewModel: AuthViewModel,
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  userViewModel: UserViewModel,
  categoryViewModel: CategoryViewModel,
  companyViewModel: CompanyViewModel,
  orderViewModel: OrderViewModel
) {
  LaunchedEffect(Unit) {
    productViewModel.loadProducts()
    cartViewModel.companies
    companyViewModel.loadCompanies()
  }

  val startDestination =
    if (authViewModel.authState.collectAsState().value == AuthState.LOGGED_IN)
      NavDestinations.HOME
    else
      NavDestinations.HOME

  NavHost(navController, startDestination = startDestination, modifier = modifier) {
    composable(NavDestinations.HOME) {
      HomeScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        //onNavigateToCart = { navController.navigate(NavDestinations.CART) },
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel,
        navController = navController
      )
    }
    composable(NavDestinations.CART) {
      CartScreen(
        cartViewModel = cartViewModel,
        userViewModel = userViewModel,
        navController = navController,
        productViewModel = productViewModel
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
          navController = navController, userViewModel = userViewModel
        )
      }
    }

    composable(NavDestinations.PROFILE) {
      ProfileScreen(
        navController = navController,
        userViewModel = userViewModel,
        authViewModel = authViewModel,
        orderViewModel = orderViewModel
      )
    }
    composable(NavDestinations.AUTH) {
      AuthScreen(
        modifier = Modifier,
        onBackClick = { navController.navigate(NavDestinations.HOME)},
        authViewModel = authViewModel,
        navController = navController,

      )
    }
    composable(NavDestinations.SEARCH) {
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        navController = navController,
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel
      )
    }
    composable(NavDestinations.CATEGORY) { backStackEntry->
      val categoryName = backStackEntry.arguments?.getString("category")
      SearchScreen(
        cartViewModel = cartViewModel,
        productViewModel = productViewModel,
        categoryName = categoryName.takeIf { it != null },
        navController = navController,
        categoryViewModel = categoryViewModel,
        userViewModel = userViewModel
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
          companies = companyViewModel.companies,
          userViewModel = userViewModel
        )
      }
    }
  }
}

