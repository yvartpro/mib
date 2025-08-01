package bi.vovota.madeinburundi.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bi.vovota.madeinburundi.R
import bi.vovota.madeinburundi.ui.section.CategoryRow
import bi.vovota.madeinburundi.ui.section.ProductList
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import bi.vovota.madeinburundi.viewmodel.CategoryViewModel
import bi.vovota.madeinburundi.viewmodel.ProductViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun SearchScreen(
  cartViewModel: CartViewModel,
  productViewModel: ProductViewModel,
  categoryName: String? = null,
  navController: NavController,
  categoryViewModel: CategoryViewModel,
  userViewModel: UserViewModel
) {
  println("Categoryname: $categoryName")
  val searchQuery = productViewModel.searchQuery
  val products = if(categoryName != null && categoryName != "tous") {
    productViewModel.products.filter {
      it.category.equals(categoryName, ignoreCase = true)
    }
  } else {
    productViewModel.filteredProducts
  }

  Column(modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .padding(16.dp)) {
    if (categoryName != null) {
      CategoryRow(navController = navController, categoryViewModel)
    } else {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { productViewModel.onSearch(it) },
        placeholder = { Text(stringResource(R.string.s_placeholder)) },
        modifier = Modifier.fillMaxWidth()
      )
    }
    Spacer(modifier = Modifier.height(16.dp))

    if (products.isEmpty()) {
      Text(stringResource(R.string.s_no_results))
    } else {
      ProductList(
        cartViewModel = cartViewModel,
        products = products,
        productViewModel = productViewModel,
        user = userViewModel.user,
        navController = navController
      )
    }
  }
}
