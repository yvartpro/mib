package bi.vovota.madeinburundi.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import bi.vovota.madeinburundi.R
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.ui.nav.NavDestinations
import bi.vovota.madeinburundi.ui.theme.FontSizes
import bi.vovota.madeinburundi.ui.theme.Spacings
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel
import kotlin.collections.chunked

@Composable
fun ProductScreen(
  product: Product,
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  navController: NavController,
  userViewModel: UserViewModel
) {
  val user by userViewModel.user.collectAsState()
  LaunchedEffect(Unit) {
    productViewModel.loadProducts()
  }
  val price = productViewModel.getPrice(product, user)
  val currency = productViewModel.getCurrency(user)
  val products = productViewModel.products
  val relatedProducts = products.filter { it.category == product.category && it.id != product.id }
  BackHandler {
    val popped = navController.popBackStack("home", inclusive = false)
    if (!popped) {
      navController.navigate("home")
    }
  }
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    // Product Details Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(Spacings.large())
    ) {
      AsyncImage(
        model = product.image,
        contentDescription = product.name,
        contentScale = ContentScale.Fit,
        modifier = Modifier
          .weight(1f)
          .aspectRatio(1f)
          .clip(RoundedCornerShape(8.dp))
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .align(Alignment.CenterVertically)
      ) {
        Text(
          text = product.name ?: stringResource(R.string.p_unknown),
          fontSize = FontSizes.caption(),
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = product.company.name,
          fontSize = FontSizes.body(),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "$price $currency ${if (product.isBox) " / box" else ""}",
          fontSize = FontSizes.caption(),
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(Spacings.medium())
    ) {
      Button(
        onClick = { cartViewModel.addToCart(product) },
        modifier = Modifier.weight(1f)
      ) {
        Icon(Icons.Default.ShoppingCart, contentDescription = "Panier")
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(R.string.p_add), fontSize = FontSizes.caption())
      }

      Button(
        onClick = {
          cartViewModel.addToCart(product)
          navController.navigate(NavDestinations.CART) {
            popUpTo(NavDestinations.HOME) { inclusive = false}
            launchSingleTop = true
          }
        },
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
      ) {
        Icon(painter = painterResource(R.drawable.checkout), contentDescription = "Buy now")
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(R.string.p_buy), fontSize = FontSizes.caption())
      }
    }

    // Divider
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = stringResource(R.string.p_same_cat),
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(vertical = 8.dp)
    )

    // Related Products
    RelatedProducts(products = relatedProducts, navController = navController)
  }
}

@Composable
fun RelatedProducts(
  navController: NavController,
  products: List<Product>
) {
  Column(verticalArrangement = Arrangement.spacedBy(Spacings.large())) {
    products.chunked(2).forEach { rowItems ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacings.large())
      ) {
        rowItems.forEach { product ->
          ProductImageItem(
            product = product,
            navController = navController,
            modifier = Modifier.weight(1f)
          )
        }
        if (rowItems.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }
  }
}

@Composable
fun ProductImageItem(
  product: Product,
  navController: NavController,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(8.dp),
    modifier = modifier
      .aspectRatio(1f)
      .clickable {
        navController.navigate("product/${product.id}") {
          popUpTo(NavDestinations.HOME)
        }
      }
  ) {
    AsyncImage(
      model = product.image,
      contentDescription = product.name,
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize()
    )
  }
}
