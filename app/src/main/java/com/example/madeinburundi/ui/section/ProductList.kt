package com.example.madeinburundi.ui.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.madeinburundi.R
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.ui.theme.FontSizes
import com.example.madeinburundi.ui.theme.Spacings
import com.example.madeinburundi.viewmodel.CartViewModel
import com.example.madeinburundi.viewmodel.ProductViewModel

@Composable
fun ProductList(
  cartViewModel: CartViewModel,
  products: List<Product>,
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    products.chunked(2).forEach { rowItems ->
      Row(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        for (product in rowItems) {
          ProductCard(
            cartViewModel =  cartViewModel,
            product = product,
            modifier = Modifier.weight(1f),
            navController = navController
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
fun ProductCard(
  cartViewModel: CartViewModel,
  product: Product,
  modifier: Modifier = Modifier,
  navController: NavController
) {
  Card(
    modifier = modifier
      .width(IntrinsicSize.Max)
      .clickable { navController.navigate("product/${product.id}") },
    shape = RoundedCornerShape(4.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
  ) {
    Column(
      modifier = Modifier.padding(Spacings.medium())
    ) {
      AsyncImage(
        model = product.image,
        contentDescription = product.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .clip(RoundedCornerShape(4.dp))
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = product.name,
        fontSize = FontSizes.caption(),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.heightIn(min = 16.sp.value.dp)
      )

      // Company Name - subtle
      if (product.company.name.isNotBlank()) {
        Spacer(modifier = Modifier.height(0.dp))
        Text(
          text = product.company.name,
          fontSize = FontSizes.caption(),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(0.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "${product.price} FC",
          fontSize = FontSizes.caption(),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.primary
        )
        FilledIconButton(
          onClick = { cartViewModel.addToCart(product) },
          shape = CircleShape,
          modifier = Modifier.size(36.dp),
          colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        ) {
          Icon(
            painter = painterResource(id = R.drawable.add_to_cart),
            contentDescription = "Add ${product.name} to cart",
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
