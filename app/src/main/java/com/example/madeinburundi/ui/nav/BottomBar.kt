package com.example.madeinburundi.ui.nav


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.ui.graphics.Color
import com.example.madeinburundi.ui.theme.GreenMIB

@Composable
fun BottomBar(navController: NavController, cartItemCount: Int) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  val showBottomBar = bottomNavItems.any { it.route == currentRoute }

  if (showBottomBar) {
    NavigationBar {
      bottomNavItems.forEach { item ->
        val selected = currentRoute == item.route
        NavigationBarItem(
          selected = selected,
          onClick = {
            if (currentRoute != item.route) {
              navController.navigate(item.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                  saveState = true
                }
                launchSingleTop = true
                restoreState = true
              }
            }
          },
          icon = {
            if (item.route == NavDestinations.CART && cartItemCount > 0) {
              BadgedBox(badge = { Badge { Text(text = "$cartItemCount", color = MaterialTheme.colorScheme.onPrimary) } }) {
                Icon(
                  painter = painterResource(id = item.icon),
                  contentDescription = item.label
                )
              }
            } else {
              Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.label
              )
            }
          },
          label = { Text(item.label) },
          alwaysShowLabel = true,
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = GreenMIB,
            selectedTextColor = GreenMIB,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent
          )
        )

      }
    }
  }
}
