package com.example.madeinburundi.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.madeinburundi.R
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.data.model.UserFields
import com.example.madeinburundi.data.model.UserUpdate
import com.example.madeinburundi.ui.component.LogoutButton
import com.example.madeinburundi.ui.nav.NavDestinations
import com.example.madeinburundi.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
  navController: NavController,
  userViewModel: UserViewModel
) {
  val user = userViewModel.user
  val isLoading = userViewModel.isLoading
  val error = userViewModel.error
  val imageUri = userViewModel.selectedImageUri

  var fullNameState by remember(user?.fullName) { mutableStateOf(user?.fullName ?: "") }
  var phoneState by remember(user?.phone) { mutableStateOf(user?.phone ?: "") }
  var addressState by remember(user?.address) { mutableStateOf(user?.address ?: "") }
  var passwordState by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    userViewModel.loadUserProfile()
  }

  LaunchedEffect(Unit) {
    userViewModel.navigateToLogin.collect {
      navController.navigate(NavDestinations.AUTH) {
        popUpTo(NavDestinations.PROFILE) { inclusive = true }
      }
    }
  }

  val imagePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri -> uri?.let { userViewModel.onImageSelected(it) } }

  when {
    isLoading -> Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator()
    }

    error != null -> Text("Erreur : $error", modifier = Modifier.padding(16.dp))

    user != null -> Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AsyncImage(
        model = imageUri ?: user.photo,
        contentDescription = "Profile Image",
        modifier = Modifier
          .size(120.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant)
          .clickable { imagePickerLauncher.launch("image/*") },
        contentScale = ContentScale.Crop
      )

      IconButton(onClick = { userViewModel.uploadImage(user.id) }) {
        Icon(Icons.Default.Edit, contentDescription = "Upload", tint = MaterialTheme.colorScheme.primary)
      }

      Spacer(modifier = Modifier.height(24.dp))

      EditableProfileInfoItem(
        label = "Nom complet",
        value = fullNameState,
        icon = Icons.Default.Person,
        onSubmit = {
          fullNameState = it
          userViewModel.updateUser(UserUpdate(UserFields(fullName = it)), user.id)
        }
      )

      EditableProfileInfoItem(
        label = "Numéro de téléphone",
        value = phoneState,
        icon = Icons.Default.Phone,
        onSubmit = {
          phoneState = it
          //userViewModel.updateUser(UserUpdate(UserFields(phone = it)), user.id)
        }
      )

      EditableProfileInfoItem(
        label = "Adresse",
        value = addressState,
        icon = Icons.Default.LocationOn,
        onSubmit = {
          addressState = it
          //userViewModel.updateUser(UserUpdate(UserFields(address = it)), user.id)
        }
      )

      EditableProfileInfoItem(
        label = "Mot de passe",
        value = passwordState,
        icon = Icons.Default.Lock,
        placeholder = "Nouveau mot de passe",
        onSubmit = {
          passwordState = ""
          //userViewModel.updateUser(UserUpdate(UserFields(password = it)), user.id)
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

      Spacer(modifier = Modifier.height(32.dp))
      Text(
        text = "Déconnexion",
        modifier = Modifier
          .align(alignment = Alignment.End)
          .clickable {
            scope.launch {
              TokenManager.clearTokens()
              userViewModel.clearUser()
              navController.navigate(NavDestinations.HOME)
            }
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

      if (isEditing) {
        IconButton(onClick = {
          onSubmit(textState)
          isEditing = false
        }) {
          Icon(Icons.Default.Check, contentDescription = "Submit", tint = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = {
          isEditing = false
        }) {
          Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error)
        }
      } else {
        IconButton(onClick = {
          textState = value
          isEditing = true
        }) {
          Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
        }
      }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
  }
}