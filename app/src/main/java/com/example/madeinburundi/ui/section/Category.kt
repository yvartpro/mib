package com.example.madeinburundi.ui.section

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.madeinburundi.data.model.Category
import com.example.madeinburundi.viewmodel.CategoryViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryRow(navController: NavController, viewModel: CategoryViewModel) {
  val categories = viewModel.categories
  BackHandler {
    val popped = navController.popBackStack("home", inclusive = false)
    if (!popped) {
      navController.navigate("home")
    }
  }
  FlowRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
  ) {
    for (category in categories) {
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
      .padding(vertical = 2.dp, horizontal = 4.dp),
    style = MaterialTheme.typography.bodyMedium,
    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    textDecoration = if (isActive) TextDecoration.Underline else null
  )
}
