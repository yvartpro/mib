package bi.vovota.madeinburundi.ui.section

import ProductImageShimmerItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import bi.vovota.madeinburundi.data.model.Product
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun ProductImageRow(
  productViewModel: ProductViewModel,
  navController: NavController,
  userViewModel: UserViewModel
) {
  val user = userViewModel.user
  val isLoading = productViewModel.isLoading
  val products = productViewModel.products.shuffled()
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
  ) {
    if (isLoading) {
      items(5) { ProductImageShimmerItem() }
    } else {
      items(products) { product ->
          ProductImageItem(product, navController, user, productViewModel)
      }
    }
  }
}
@Composable
fun ProductImageItem(
  product: Product,
  navController: NavController,
  user: User?,
  productViewModel: ProductViewModel,
  ) {
  val price = productViewModel.getPrice(product, user)
  val currency = productViewModel.getCurrency(user)

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(80.dp)
      .clickable { navController.navigate("product/${product.id}") }
  ) {
    Surface(
      shape = MaterialTheme.shapes.medium,
      color = MaterialTheme.colorScheme.surfaceContainerHighest,
      tonalElevation = 1.dp,
      modifier = Modifier
        .size(80.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        AsyncImage(
          model = product.image,
          contentDescription = product.name,
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
    Spacer(Modifier.height(4.dp))
    Text(
      text = "$price $currency",
      fontSize = 10.sp,
      fontWeight = FontWeight.SemiBold,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}


