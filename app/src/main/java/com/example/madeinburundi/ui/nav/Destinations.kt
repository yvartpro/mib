package com.example.madeinburundi.ui.nav

import androidx.annotation.DrawableRes
import com.example.madeinburundi.R

object NavDestinations {
  const val AUTH = "auth"
  const val HOME = "home"
  const val CART = "cart"
  const val COMPANIES = "companies"
  const val COMPANY = "company/{companyId}"
  const val PROFILE = "profile"
  const val SEARCH = "search"
  const val PRODUCT = "product/{productId}"
  const val CATEGORY = "search/{category}"
}

data class BottomNavItem(
  val label: String,
  @DrawableRes val icon: Int,
  val route: String
)

val bottomNavItems = listOf(
  BottomNavItem("Accueil", R.drawable.home, NavDestinations.HOME),
  BottomNavItem("Industry", R.drawable.business, NavDestinations.COMPANIES),
  BottomNavItem("Panier", R.drawable.shopping_cart, NavDestinations.CART),
  BottomNavItem("Profil", R.drawable.person, NavDestinations.PROFILE)
)
