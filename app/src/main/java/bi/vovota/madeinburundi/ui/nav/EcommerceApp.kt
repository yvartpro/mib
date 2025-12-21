package bi.vovota.madeinburundi.ui.nav

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import bi.vovota.madeinburundi.ui.component.SnackbarTool
import bi.vovota.madeinburundi.viewmodel.AuthViewModel
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import bi.vovota.madeinburundi.viewmodel.CategoryViewModel
import bi.vovota.madeinburundi.viewmodel.CompanyViewModel
import bi.vovota.madeinburundi.viewmodel.OrderViewModel
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel
import kotlin.system.exitProcess

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun EcommerceApp(
  cartViewModel: CartViewModel = hiltViewModel(),
  productViewModel: ProductViewModel = hiltViewModel(),
  authViewModel: AuthViewModel = hiltViewModel(),
  companyViewModel: CompanyViewModel = hiltViewModel(),
  userViewModel: UserViewModel = hiltViewModel(),
  categoryViewModel: CategoryViewModel = hiltViewModel(),
  orderViewModel: OrderViewModel = hiltViewModel()
) {
  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val context = LocalContext.current
  val activity = context as? Activity
  if (currentRoute == NavDestinations.HOME) {
    BackHandler { activity?.finish() }
  }
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
        topPadding = 100.dp
      )
    },
    topBar = {
      if (currentRoute != NavDestinations.AUTH) {
        TopBar(
          navController = navController,
          authViewModel = authViewModel,
          userViewModel = userViewModel,
            cartViewModel = cartViewModel,
            categoryViewModel = categoryViewModel
        )
      }
    },
    bottomBar = {
      BottomBar(navController = navController, cartItemCount = cartCount, cartViewModel)
    }
  ) { innerPadding ->
    AppNavGraph(
      modifier = Modifier.padding(innerPadding),
      navController = navController,
      authViewModel = authViewModel,
      cartViewModel = cartViewModel,
      productViewModel = productViewModel,
      companyViewModel = companyViewModel,
      userViewModel = userViewModel,
      categoryViewModel = categoryViewModel,
      orderViewModel = orderViewModel
    )
  }
}
