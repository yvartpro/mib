package bi.vovota.madeinburundi.ui.section

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bi.vovota.madeinburundi.data.model.Category
import bi.vovota.madeinburundi.viewmodel.CategoryViewModel


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
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(vertical = 8.dp) // Padding for the row itself
  ) {
    items(categories.size) { index ->
      val category = categories[index]
      CategoryItem(
        category = category,
        isActive = category.isActive,
        onClick = {
          viewModel.setActive(category.name)
          navController.navigate("search/${category.name}") {
            popUpTo("home") {
              inclusive = false
            }
            launchSingleTop = true
          }
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
    text = stringResource(category.title),
    fontWeight = FontWeight(if (isActive) 700 else 800),
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(vertical = 2.dp, horizontal = 4.dp),
    style = MaterialTheme.typography.bodyMedium,
    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
    textDecoration = if (isActive) TextDecoration.Underline else null
  )
}