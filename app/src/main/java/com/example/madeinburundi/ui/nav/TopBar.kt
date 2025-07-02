package com.example.madeinburundi.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.madeinburundi.R

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TopBar(
  navController: NavController,
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

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