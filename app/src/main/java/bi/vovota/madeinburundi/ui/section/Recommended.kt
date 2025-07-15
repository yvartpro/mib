package bi.vovota.madeinburundi.ui.section

import RecommendedShimmerItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.ui.theme.FontSizes
import bi.vovota.madeinburundi.ui.theme.Spacings
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun Recommended(
  productViewModel: ProductViewModel,
  navController: NavController,
  userViewModel: UserViewModel
) {
  val user = userViewModel.user
  val isLoading = productViewModel.isLoading
  val products = productViewModel.products
  val chunks = if (isLoading) List(5) { listOf(null, null) } else products.chunked(2)

  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    contentPadding = PaddingValues(horizontal = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(chunks) { columnProducts ->
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.width(180.dp)
      ) {
        columnProducts.forEach { product ->
          if (product == null) {
            RecommendedShimmerItem()
          } else {
              ProductItem(productViewModel, product, navController, user)
          }
        }
      }
    }
  }
}

@Composable
fun ProductItem(
  productViewModel: ProductViewModel,
  product: Product,
  navController: NavController,
  user : User?
) {
  val price = productViewModel.getPrice(product, user)
  val currency = productViewModel.getCurrency(user)

  Row(
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .background(Color.Transparent)
      .padding(horizontal = Spacings.medium())
      .width(180.dp)
      .clickable { navController.navigate("product/${product.id}")}
  ) {
    AsyncImage(
      model = product.image,
      contentDescription = "${product.name} logo",
      modifier = Modifier
        .size(60.dp)
        .clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.Fit
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = product.name, fontSize = FontSizes.caption())
      Text(text = "$price $currency", fontSize = FontSizes.caption(),  color = Color.Gray)
      Text(text = product.company.name, fontSize = FontSizes.caption(), color = Color.DarkGray)
    }
  }
}