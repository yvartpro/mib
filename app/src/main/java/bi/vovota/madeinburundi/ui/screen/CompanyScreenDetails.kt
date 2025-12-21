package bi.vovota.madeinburundi.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import bi.vovota.madeinburundi.data.model.Company
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.ui.section.ProductCard
import bi.vovota.madeinburundi.ui.theme.FontSizes
import bi.vovota.madeinburundi.ui.theme.Spacings
import bi.vovota.madeinburundi.viewmodel.AuthViewModel
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun CompanyDetailsScreen(
  companyId: Int,
  navController: NavController,
  companies: List<Company>,
  productViewModel: ProductViewModel,
  cartViewModel: CartViewModel,
  userViewModel: UserViewModel,
) {
  val user by userViewModel.user.collectAsState()

  val company = companies.find { it.id == companyId }
  val companyProducts = productViewModel.products.filter { it.company.id == companyId }
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    company?.let {
      Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
          model = company.logo,
          contentDescription = company.name,
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
          contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Text(company.name, fontSize = FontSizes.body(), fontWeight = FontWeight.SemiBold)
          company.slogan?.let {
            Text(text = it, fontSize = FontSizes.caption(), color = Color.Gray, style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 12.sp))
          }
        }
      }
      Spacer(Modifier.height(12.dp))
      company.description?.let {
        Text(it, fontSize = FontSizes.caption())
      }
      Spacer(Modifier.height(24.dp))
val nam = company.name
      Text(
        text = stringResource(bi.vovota.madeinburundi.R.string.co_made_by, nam),
        fontSize = FontSizes.body(),
        fontWeight = FontWeight.Normal
      )
      Spacer(Modifier.height(12.dp))
      if (companyProducts.isEmpty()) {
        Text(stringResource(bi.vovota.madeinburundi.R.string.co_no_prod))
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(Spacings.large())) {
          companyProducts.chunked(2).forEach { rowItems ->
            Row(
              modifier = Modifier
                .fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              for (product in rowItems){
                ProductCard(
                  product = product,
                  cartViewModel = cartViewModel,
                  modifier = Modifier.weight(1f),
                  user = user,
                  navController = navController,
                  productViewModel = productViewModel
                )
              }
            }
          }
        }
      }
    } ?: Text(stringResource(bi.vovota.madeinburundi.R.string.co_missing))
  }
}
