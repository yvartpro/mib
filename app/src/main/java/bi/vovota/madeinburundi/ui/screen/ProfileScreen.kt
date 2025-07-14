package bi.vovota.madeinburundi.ui.screen

import ProfileShimmer
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bi.vovota.madeinburundi.R
import bi.vovota.madeinburundi.data.model.Order
import bi.vovota.madeinburundi.data.model.UserFields
import bi.vovota.madeinburundi.data.model.UserUpdate
import bi.vovota.madeinburundi.ui.component.MyDropDownMenu
import bi.vovota.madeinburundi.ui.nav.NavDestinations
import bi.vovota.madeinburundi.ui.theme.FontSizes
import bi.vovota.madeinburundi.viewmodel.OrderViewModel
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
  navController: NavController,
  userViewModel: UserViewModel,
  orderViewModel: OrderViewModel
) {
  val context = LocalContext.current
  val user = userViewModel.user
  val isLoading = userViewModel.isLoading
  val error = userViewModel.error
  val isEditMode = userViewModel.isEditMode
  val isUpdating = userViewModel.isUpdating

  var fullNameState by remember(user?.fullName) { mutableStateOf(user?.fullName ?: "") }
  var phoneState by remember(user?.phone) { mutableStateOf(user?.phone ?: "") }
  var addressState by remember(user?.address) { mutableStateOf(user?.address ?: "") }
  var passwordState by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    userViewModel.loadUserProfile()
    orderViewModel.loadOrders()
    userViewModel.navigateToLogin.collect {
      navController.navigate(NavDestinations.AUTH)
    }
  }
    val orders = orderViewModel.orders
    val ownOrders = orders.filter { it.customer == user?.id }.sortedByDescending { it.date }
  if (isLoading) {
    ProfileShimmer()
  } else if (error != null) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(stringResource(R.string.pr_err))
    }
  } else if (user != null) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
val name = user.fullName
Text(
  text = stringResource(R.string.pr_welcome,name),
  style = MaterialTheme.typography.headlineSmall.copy(
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.Bold
  ),
  modifier = Modifier
    .padding(vertical = 16.dp)
    .fillMaxWidth(),
  textAlign = androidx.compose.ui.text.style.TextAlign.Center
)
      Spacer(modifier = Modifier.height(24.dp))

      EditableProfileInfoItem(
        isEditMode = isEditMode,
        isUpdating = isUpdating,
        label = stringResource(R.string.f_name),
        value = fullNameState,
        icon = Icons.Default.Person,
        onSubmit = {
          fullNameState = it
          userViewModel.updateUser(UserUpdate(UserFields(it, null, null), null), user.id)
        }
      )

        if (!isEditMode) {
            EditableProfileInfoItem(
                label = stringResource(R.string.f_phone),
                isEditMode = isEditMode,
                isUpdating = isUpdating,
                value = phoneState,
                icon = Icons.Default.Phone,
                onSubmit = {
                    phoneState = it
                    userViewModel.updateUser(UserUpdate(UserFields(null, it, null), null), user.id)
                }
            )
        }

      EditableProfileInfoItem(
        label = stringResource(R.string.f_addr),
        isEditMode = isEditMode,
        isUpdating = isUpdating,
        value = addressState,
        icon = Icons.Default.LocationOn,
        onSubmit = {
          addressState = it
          userViewModel.updateUser(UserUpdate(UserFields(null, null, null), address = it), user.id)
        }
      )

        if (isEditMode) {
            EditableProfileInfoItem(
                isEditMode = isEditMode,
                isUpdating = isUpdating,
                label = stringResource(R.string.f_pwd),
                value = passwordState,
                icon = Icons.Default.Lock,
                placeholder = "",
                onSubmit = {
                    passwordState = it
                    userViewModel.updateUser(UserUpdate(UserFields(null, null, password = it), null), user.id)
                    passwordState = ""
                },
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility),
                            contentDescription = "Toggle password"
                        )
                    }
                }
            )
        }
      if (ownOrders.isNotEmpty()) {
        OrderTable(ownOrders)
      }else{
        Text(stringResource(R.string.pr_no_command))
      }
      Spacer(modifier = Modifier.height(32.dp))

      Text(
        text = stringResource(R.string.pr_version),
        fontSize = FontSizes.caption()
      )
      Text(
        text = stringResource(R.string.pr_vovota),
        fontSize = FontSizes.caption(),
        color = Color(0xFF37a8ee),
        modifier = Modifier.clickable {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vovota.bi/about"))
          context.startActivity(intent)
        }
      )
    }
  }
}

@Composable
fun EditableProfileInfoItem(
  label: String,
  value: String,
  icon: ImageVector,
  isEditMode: Boolean,
  isUpdating: Boolean,
  onSubmit: (String) -> Unit,
  placeholder: String = "",
  keyboardType: KeyboardType = KeyboardType.Text,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  trailingIcon: @Composable (() -> Unit)? = null
) {
  var isEditing by remember { mutableStateOf(false) }
  var textState by remember { mutableStateOf("") }

  Column(modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 12.dp)) {

    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        modifier = Modifier.size(28.dp),
        tint = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = label,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isEditing) {
          TextField(
            value = textState,
            onValueChange = { textState = it },
            placeholder = { Text(placeholder) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              disabledContainerColor = Color.Transparent,
              focusedIndicatorColor = Color.Transparent,
              unfocusedIndicatorColor = Color.Transparent,
              disabledIndicatorColor = Color.Transparent,
              cursorColor = MaterialTheme.colorScheme.primary,
              focusedTextColor = MaterialTheme.colorScheme.onSurface,
              unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 4.dp, start = 0.dp)
          )
        } else {
          Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }

      if (isEditMode) {
        if (isEditing) {
          IconButton(onClick = {
            onSubmit(textState)
            isEditing = false
          }) {
            if (isUpdating) {
              CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
              Icon(
                Icons.Default.Check,
                contentDescription = "Submit",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
          if (!isUpdating) {
            IconButton(onClick = {
              isEditing = false
            }) {
              Icon(
                Icons.Default.Close,
                contentDescription = "Cancel",
                tint = MaterialTheme.colorScheme.error
              )
            }
          }
        } else {
          IconButton(onClick = {
            textState = value
            isEditing = true
          }) {
            Icon(
              Icons.Default.Edit,
              contentDescription = "Edit",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
  }
}

@Composable
fun OrderTable(orders: List<Order>) {
  val categories = listOf(
    OrderState(stringResource(R.string.pr_paid), "isPaid"),
    OrderState(stringResource(R.string.pr_delivred), "isDelivered"),
    OrderState(stringResource(R.string.pr_no_paid), "isNotPaid"),
    OrderState(stringResource(R.string.pr_delivred_not), "isNotDelivered"),
  )

  var selected by remember { mutableStateOf<String?>(null) }

  val filteredOrders = remember(orders, selected) {
    when (selected) {
      "isPaid" -> orders.filter { it.isPayed }
      "isNotPaid" -> orders.filter { !it.isPayed }
      "isDelivered" -> orders.filter { it.isDelivered }
      "isNotDelivered" -> orders.filter { !it.isDelivered }
      else -> orders
    }
  }

  Column {
    Spacer(modifier = Modifier.height(24.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = stringResource(R.string.pr_your_command),
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = FontSizes.body()
        )
      )

      MyDropDownMenu(
        options = categories,
        selectedOption = selected,
        onOptionSelected = { selected = it }
      )
    }

    filteredOrders.forEach { order ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = stringResource(R.string.pr_command_no),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = order.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = if (order.isDelivered) stringResource(R.string.pr_delivred) else stringResource(R.string.pr_delivred_not),
              color = if (order.isDelivered) Color(0xFF4CAF50) else Color(0xFFF57C00),
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
            Text(
              text = if (order.isPayed) stringResource(R.string.pr_paid) else stringResource(R.string.pr_no_paid),
              color = if (order.isPayed) Color(0xFF4CAF50) else Color(0xFFD32F2F),
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
          }
        }
      }
    }
  }
}

data class OrderState(
  val title: String,
  val value: String
)