package com.example.madeinburundi.ui.screen

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.madeinburundi.viewmodel.CartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.madeinburundi.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.madeinburundi.data.model.CartItem
import com.example.madeinburundi.data.repository.OrderRepository
import com.example.madeinburundi.ui.nav.NavDestinations
import com.example.madeinburundi.ui.theme.FontSizes
import com.example.madeinburundi.ui.theme.GreenMIB
import com.example.madeinburundi.viewmodel.OrderViewModel
import com.example.madeinburundi.viewmodel.UserViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
  cartViewModel: CartViewModel,
  userViewModel: UserViewModel,
  navController: NavController,
  onBack: () -> Unit
) {
  val cartItems = cartViewModel.cartItems
  val totalAmount = cartViewModel.grandTotal
  val isCheckoutLoading = cartViewModel.isCheckingOut
  var showCheckoutDialog by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  var confirmedItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
  var confirmedTotal by remember { mutableDoubleStateOf(0.0) }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      TopAppBar(
        title = { Text("Panier", fontWeight = FontWeight.Normal) },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surfaceContainer,
          titleContentColor = MaterialTheme.colorScheme.onSurface,
          navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
          actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      if (cartItems.isEmpty()) {
        EmptyCartPlaceholder()
      } else {
        Text("Articles dans le panier", fontSize = FontSizes.body(), modifier = Modifier.padding(vertical = 8.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          cartItems.forEach { cartItem ->
            ModernCartItem(
              cartItem = cartItem,
              onIncreaseQuantity = { cartViewModel.increaseQuantity(cartItem.product.id) },
              onDecreaseQuantity = { cartViewModel.decreaseQuantity(cartItem.product.id) },
              onRemoveItem = { cartViewModel.removeItem(cartItem.product.id) }
            )
          }
        }

        CheckoutSummarySection(
          totalAmount = totalAmount,
          onCheckoutClick = {
            println("🛒 Checkout clicked")
            Log.d("CartScreen", "🛒 Checkout clicked. Total: $totalAmount, Items: ${cartItems.size}")

            val itemsCopy = cartItems.map { it.copy() }
            val totalCopy = totalAmount


            if (userViewModel.user != null) {
              cartViewModel.checkoutAndClear { success ->
                Log.d("CartScreen", "✅ Checkout result: $success")

                if (success) {
                  confirmedItems = itemsCopy
                  confirmedTotal = totalCopy
                  showCheckoutDialog = true

                  coroutineScope.launch {
                    cartViewModel.notifyAdded("Commande réussie")
                    snackbarHostState.showSnackbar(message = "Commande réussie", withDismissAction = true)
                  }
                } else {
                  coroutineScope.launch {
                    snackbarHostState.showSnackbar(message = "La commande a échouée. Veuillez réessayer.", withDismissAction = true)
                  }
                }
              }
            }else{
              navController.navigate(NavDestinations.AUTH)
            }
          }
          ,
          isLoading = isCheckoutLoading,
          modifier = Modifier.padding(top = 24.dp),
        )
      }
    }
  }

  if (showCheckoutDialog) {
    FancyCheckoutDialog(
      cartItems = confirmedItems,
      totalAmount = confirmedTotal,
      onDismiss = { showCheckoutDialog = false }
    )
  }
}


@Composable
fun ModernCartItem(
  cartItem: CartItem,
  onIncreaseQuantity: () -> Unit,
  onDecreaseQuantity: () -> Unit,
  onRemoveItem: () -> Unit
) {
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
        contentScale = ContentScale.Crop,
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
          text = "${cartItem.product.price} FC",
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
  onDismiss: () -> Unit
) {
  val orderSummary = remember(cartItems, totalAmount) {
    buildString {
      append("Détails de la commande:\n")
      cartItems.forEach {
        val name = it.product.name
        val unitPrice = it.product.price.toDouble()
        val quantity = it.quantity.value
        val subtotal = unitPrice * quantity
        append("- ${quantity}x $name (${unitPrice.toInt()} FC): ${subtotal.toInt()} FC\n")
      }
      append("\nTotal: ${totalAmount.toInt()} FC")
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("🎉 Commande confirmée", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    },
    text = {
      Text(orderSummary, style = MaterialTheme.typography.bodySmall)
    },
    confirmButton = {
      Button(onClick = onDismiss) {
        Text(text = "Ok", fontSize = FontSizes.caption() )
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
        "Votre panier est vide",
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
  modifier: Modifier = Modifier
) {
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
          "Total à payer",
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold
        )

        Text(
          "FC ${totalAmount.toInt()}",
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
          Text("Passer la commande")
        }
      }
    }
  }
}
