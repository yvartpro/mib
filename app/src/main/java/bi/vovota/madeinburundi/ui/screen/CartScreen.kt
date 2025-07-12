package bi.vovota.madeinburundi.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import bi.vovota.madeinburundi.viewmodel.CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import bi.vovota.madeinburundi.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bi.vovota.madeinburundi.data.model.CartItem
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.ui.nav.NavDestinations
import bi.vovota.madeinburundi.ui.theme.FontSizes
import bi.vovota.madeinburundi.ui.theme.GreenMIB
import bi.vovota.madeinburundi.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun CartScreen(
  cartViewModel: CartViewModel,
  userViewModel: UserViewModel,
  navController: NavController,
  onBack: () -> Unit
) {
  BackHandler {
    val popped = navController.popBackStack("home", inclusive = false)
    if (!popped) {
      navController.navigate("home")
    }
  }
  val cartItems = cartViewModel.cartItems
  val totalAmount = cartViewModel.calculateGrandTotal(userViewModel.user)
  val isCheckoutLoading = cartViewModel.isCheckingOut
  var showCheckoutDialog by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  var confirmedItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
  var confirmedTotal by remember { mutableDoubleStateOf(0.0) }
  val user = userViewModel.user

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      if (cartItems.isEmpty()) {
        EmptyCartPlaceholder()
      } else {
        Text(stringResource(R.string.ca_title), fontSize = FontSizes.body(), modifier = Modifier.padding(vertical = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          cartItems.forEach { cartItem ->
            ModernCartItem(
              cartItem = cartItem,
              onIncreaseQuantity = { cartViewModel.increaseQuantity(cartItem.product.id) },
              onDecreaseQuantity = { cartViewModel.decreaseQuantity(cartItem.product.id) },
              onRemoveItem = { cartViewModel.removeItem(cartItem.product.id) },
              user = userViewModel.user
            )
          }
        }

        CheckoutSummarySection(
          totalAmount = totalAmount,
          onCheckoutClick = {
            val itemsCopy = cartItems.map { it.copy() }

            if (user != null) {
              cartViewModel.checkoutAndClear { success ->
                if (success) {
                  confirmedItems = itemsCopy
                  confirmedTotal = totalAmount
                  showCheckoutDialog = true
                } else {
                  coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = "La commande a échouée. Veuillez réessayer.", withDismissAction = true)
                  }
                }
              }
            }else{
              coroutineScope.launch {
                userViewModel.loadUserProfile()
                navController.navigate(NavDestinations.AUTH)
              }
            }
          }
          ,
          isLoading = isCheckoutLoading,
          modifier = Modifier.padding(top = 24.dp),
          user = user
        )
      }
    }
  //}

  if (showCheckoutDialog) {
    FancyCheckoutDialog(
      cartItems = confirmedItems,
      totalAmount = confirmedTotal,
      onDismiss = { showCheckoutDialog = false },
      user = user
    )
  }
}


@Composable
fun ModernCartItem(
  cartItem: CartItem,
  onIncreaseQuantity: () -> Unit,
  onDecreaseQuantity: () -> Unit,
  onRemoveItem: () -> Unit,
  user: User?
) {
  fun getPrice(): String {
    return when (user?.code) {
      "254" -> cartItem.product.kePrice
      "255" -> cartItem.product.tzPrice
      "250" -> cartItem.product.rwPrice
      "243" -> cartItem.product.drcPrice
      "256" -> cartItem.product.ugPrice
      else -> cartItem.product.bdiPrice
    }
  }
  fun getCurrency(): String {
    return when (user?.code) {
      "254" -> "KSH"
      "255" -> "TSH"
      "250" -> "RWF"
      "243" -> "FC"
      "256" -> "UGX"
      else -> "FBU"
    }
  }
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Row(
      modifier = Modifier
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        model = cartItem.product.image,
        contentDescription = cartItem.product.name,
        contentScale = ContentScale.Fit,
        modifier = Modifier
          .size(72.dp)
          .clip(RoundedCornerShape(12.dp))
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(
        modifier = Modifier
          .weight(1f)
      ) {
        Text(
          text = cartItem.product.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${getPrice()} ${getCurrency()}",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onDecreaseQuantity,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(painter = painterResource(R.drawable.remove), contentDescription = "Decrease", tint = Color.Red)
          }

          Text(
            text = "${cartItem.quantity.value}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
          )

          IconButton(
            onClick = onIncreaseQuantity,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = "Increase", tint = GreenMIB)
          }
        }
      }

      IconButton(
        onClick = onRemoveItem,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Remove",
          tint = MaterialTheme.colorScheme.error
        )
      }
    }
  }
}


@Composable
fun FancyCheckoutDialog(
  cartItems: List<CartItem>,
  totalAmount: Double,
  onDismiss: () -> Unit,
  user: User?
) {
  fun getCurrency(): String {
    return when (user?.code) {
      "254" -> "KSH"
      "255" -> "TSH"
      "250" -> "RWF"
      "243" -> "FC"
      "256" -> "UGX"
      else -> "FBU"
    }
  }
  val details = stringResource(R.string.ca_command_details)
  val orderSummary = remember(cartItems, totalAmount) {
    buildString {
      append(details)
      cartItems.forEach {
        fun getPrice(): String {
          return when (user?.code) {
            "254" -> it.product.kePrice
            "255" -> it.product.tzPrice
            "250" -> it.product.rwPrice
            "243" -> it.product.drcPrice
            "256" -> it.product.ugPrice
            else -> it.product.bdiPrice
          }
        }
        val name = it.product.name
        val unitPrice = getPrice().toDouble()
        val quantity = it.quantity.value
        val subtotal = unitPrice * quantity
        append("- ${quantity}x $name (${unitPrice.toInt()} ${getCurrency()}): ${subtotal.toInt()}  ${getCurrency()}\n")
      }
      append("\nTotal: ${totalAmount.toInt()}  ${getCurrency()}")
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(stringResource(R.string.ca_command_confirm), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    },
    text = {
      Text(orderSummary, style = MaterialTheme.typography.bodySmall)
    },
    confirmButton = {
      Button(onClick = onDismiss) {
        Text(text = stringResource(R.string.ok), fontSize = FontSizes.caption() )
      }
    }
  )
}

@Composable
fun EmptyCartPlaceholder() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(top = 100.dp),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        painter = painterResource(R.drawable.add_to_cart),
        contentDescription = "Panier vide",
        tint = Color.Gray,
        modifier = Modifier.size(80.dp)
      )
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        stringResource(R.string.ca_empty),
        style = MaterialTheme.typography.titleMedium,
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}


@Composable
fun CheckoutSummarySection(
  totalAmount: Double,
  onCheckoutClick: () -> Unit,
  isLoading: Boolean,
  modifier: Modifier = Modifier,
  user: User?
) {
  fun getCurrency(): String {
    return when (user?.code) {
      "254" -> "KSH"
      "255" -> "TSH"
      "250" -> "RWF"
      "243" -> "FC"
      "256" -> "UGX"
      else -> "FBU"
    }
  }
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          stringResource(R.string.ca_total_pay),
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold
        )

        Text(
          "${totalAmount.toInt()} ${getCurrency()}",
          style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.primary
          ),
          fontWeight = FontWeight.ExtraBold
        )
      }
      Spacer(modifier = Modifier.height(12.dp))
      Button(
        onClick = onCheckoutClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
      ) {
        if (isLoading) {
          CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp),
            strokeWidth = 2.dp
          )
        } else {
          Text(stringResource(R.string.ca_place_command))
        }
      }
    }
  }
}
