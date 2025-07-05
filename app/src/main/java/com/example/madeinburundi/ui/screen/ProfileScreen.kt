
package com.example.madeinburundi.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.madeinburundi.R
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.ui.component.LogoutButton
import com.example.madeinburundi.ui.component.ProfileTextField
import com.example.madeinburundi.ui.nav.NavDestinations
import com.example.madeinburundi.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
  navController: NavController,
  userViewModel: UserViewModel = hiltViewModel(),
  initialIsEditMode: Boolean = false
) {
  val user = userViewModel.user
  val isLoading = userViewModel.isLoading
  val error = userViewModel.error

  LaunchedEffect(Unit) {
    userViewModel.loadUserProfile()
  }
  LaunchedEffect(Unit) {
    userViewModel.navigateToLogin.collect {
      navController.navigate(NavDestinations.AUTH) {
        popUpTo(NavDestinations.PROFILE) { inclusive = true}
      }
    }
  }
  var isEditMode by remember { mutableStateOf(initialIsEditMode) }

  // State for editable fields
  var fullNameState by remember(user?.fullName) { mutableStateOf(user?.fullName) }
  var phoneNumberState by remember(user?.phone) { mutableStateOf(user?.phone) }
  //var addressState by remember(user?.address) { mutableStateOf(user?.address) }
  var passwordState by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()

  LaunchedEffect(key1 = user, key2 = isEditMode) {
    if (!isEditMode) {
      fullNameState = user?.fullName
      phoneNumberState = user?.phone
      //addressState = user?.address
      passwordState = ""
    }
  }

  when {
    isLoading ->
      Column(
        modifier = Modifier
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ){ CircularProgressIndicator() }
    error != null ->
      Text("Error: $error")
    user != null ->

      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Profile Avatar Section
        Image(
          painter = painterResource(id = R.drawable.user),
          contentDescription = "Profile Avatar",
          modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {  },
          contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // User Information Sections
        if (isEditMode) {
          // --- Edit Mode ---
          ProfileTextField(
            value = fullNameState.toString(),
            onValueChange = { fullNameState = it },
            label = "Nom complet",
            leadingIconVector = Icons.Filled.Person,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
          )
          ProfileTextField(
            value = phoneNumberState.toString(),
            onValueChange = { phoneNumberState = it },
            label = "Numero de tétéphone",
            leadingIconVector = Icons.Filled.Phone,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
          )
//          ProfileTextField(
//            value = addressState.toString(),
//            onValueChange = { addressState = it },
//            label = "Adresse",
//            leadingIconVector = Icons.Filled.LocationOn,
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Next
//          )
          ProfileTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            label = "Mot de passe",
            leadingIconVector = Icons.Filled.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
              val description = if (passwordVisible) "Hide password" else "Show password"
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = if (passwordVisible) painterResource(R.drawable.visibility_off) else painterResource(R.drawable.visibility), description)
              }
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            isSensitive = true,
            placeholderText = "Nouveau mot de passe (facultatif)"
          )
          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = { isEditMode = false },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Filled.Add, contentDescription = "Save Icon", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Enregistrer")
          }

          // Cancel Button
          OutlinedButton(
            onClick = {
              isEditMode = false
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Annuler")
          }

        } else {
          ProfileInfoItem(label = "Nom complet", value = user.fullName, icon = Icons.Filled.Person)
          ProfileInfoItem(label = "Numéro de téléphone", value = user.phone, icon = Icons.Filled.Phone)
          //ProfileInfoItem(label = "Adresse", value = user?.address, icon = Icons.Filled.LocationOn)
          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = { isEditMode = true },
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit Icon", modifier = Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Modifier le Profil")
          }
          LogoutButton(onClick = {
            scope.launch {
              TokenManager.clearTokens()
              navController.navigate(NavDestinations.HOME)
            }
          }, modifier = Modifier, text = "Logout")
        }
      }
  }
}

@Composable
fun ProfileInfoItem(
  label: String,
  value: String,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      modifier = Modifier.size(28.dp),
      tint = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.width(16.dp))
    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = value,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
  HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}