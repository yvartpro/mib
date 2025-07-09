package com.example.madeinburundi.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.madeinburundi.R
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.ui.theme.FontSizes
import com.example.madeinburundi.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TopBar(
  navController: NavController,
  userViewModel: UserViewModel
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
  val scope = rememberCoroutineScope()

  val showBackArrow = currentRoute == NavDestinations.SEARCH ||
      currentRoute == NavDestinations.CART ||
      currentRoute == NavDestinations.CATEGORY ||
      currentRoute == NavDestinations.PRODUCT ||
      currentRoute == NavDestinations.COMPANY

  TopAppBar(
    title = {
      when (currentRoute) {
        NavDestinations.CART -> Text("Votre panier")
        NavDestinations.SEARCH -> Text("Recherche")
        NavDestinations.CATEGORY -> Text("Catégories")
        NavDestinations.PRODUCT -> Text("Produit")
        else -> {
          Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo")
        }
      }
    },
    navigationIcon = {
      if (showBackArrow) {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
        }
      }
    },
    actions = {
      if (currentRoute == NavDestinations.HOME) {
        IconButton(onClick = { navController.navigate(NavDestinations.SEARCH) }) {
          Icon(Icons.Filled.Search, contentDescription = "Search")
        }
      }else if (currentRoute == NavDestinations.PROFILE) {
        Row {
          TextButton(onClick = { userViewModel.toggleEdit() }) {
            Text(
              text = "Modifier",
              fontWeight = FontWeight.SemiBold,
              fontSize = FontSizes.body(),
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          IconButton(onClick = {
            scope.launch {
              TokenManager.clearTokens()
              userViewModel.clearUser()
              navController.navigate(NavDestinations.HOME)
            }
          }) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.onBackground)
          }
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainer,
      titleContentColor = MaterialTheme.colorScheme.onSurface,
      navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
      actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
  )
}