package com.example.madeinburundi.ui.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.madeinburundi.data.model.Category

@Composable
fun CategoryRow(navController: NavController) {
  val categories = listOf(
    Category("Tous", null, true),
    Category("Aliments", "food", false),//food
    Category("Boissons", "beverage", false),//beverage
    Category("Sovons", "soap", false),//soap
    Category("Beauté", "beauty", false),//beauty
    Category( "Electroniques", "electronic",false),//electronic
    Category("Construction", "construction", false)//construction
  )
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(vertical = 8.dp) // Padding for the row itself
  ) {
    items(categories) { category ->
      CategoryItem(
        category,
        category.isActive,
        modifier = Modifier,
        navController = navController
      )
    }
  }
}

@Composable
fun CategoryItem(
  category: Category,
  isActive: Boolean,
  modifier: Modifier = Modifier,
  navController: NavController
) {
  Text(
    text = category.title,
    fontWeight = FontWeight(if (isActive) 700 else 400),
    modifier = modifier
      .clickable(onClick = {
        navController.navigate("search/${category.name}") {
          popUpTo("home") { inclusive = false }
          launchSingleTop = true
        }
        category.isActive = true
      } )
      .padding(vertical = 8.dp, horizontal = 4.dp),
    style = MaterialTheme.typography.bodyLarge,
    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    textDecoration = if (isActive) TextDecoration.Underline else null
  )
}