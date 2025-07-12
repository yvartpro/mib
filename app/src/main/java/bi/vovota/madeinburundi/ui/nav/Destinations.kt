package bi.vovota.madeinburundi.ui.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bi.vovota.madeinburundi.R

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
  @StringRes val label: Int,
  @DrawableRes val icon: Int,
  val route: String
)

val bottomNavItems = listOf(
  BottomNavItem(R.string.b_home, R.drawable.home, NavDestinations.HOME),
  BottomNavItem(R.string.b_industry, R.drawable.business, NavDestinations.COMPANIES),
  BottomNavItem(R.string.b_cart, R.drawable.shopping_cart, NavDestinations.CART),
  BottomNavItem(R.string.b_profile, R.drawable.person, NavDestinations.PROFILE)
)