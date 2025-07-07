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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.madeinburundi.data.model.Category
import com.example.madeinburundi.viewmodel.CategoryViewModel

@Composable
fun CategoryRow(navController: NavController, viewModel: CategoryViewModel = hiltViewModel()) {
  val categories = viewModel.categories

  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(vertical = 8.dp)
  ) {
    items(categories) { category ->
      CategoryItem(
        category = category,
        isActive = category.isActive,
        onClick = {
          viewModel.setActive(category.name)
          navController.navigate("search/${category.name}")
        }
      )
    }
  }
}



@Composable
fun CategoryItem(
  category: Category,
  isActive: Boolean,
  onClick: () -> Unit
) {
  Text(
    text = category.title,
    fontWeight = FontWeight(if (isActive) 700 else 400),
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp, horizontal = 4.dp),
    style = MaterialTheme.typography.bodyLarge,
    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    textDecoration = if (isActive) TextDecoration.Underline else null
  )
}
