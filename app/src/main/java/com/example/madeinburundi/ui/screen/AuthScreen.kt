package com.example.madeinburundi.ui.screen

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.example.madeinburundi.ui.component.PhoneInputField
import com.example.madeinburundi.ui.component.ProfileTextField
import com.example.madeinburundi.ui.component.SmallText
import com.example.madeinburundi.ui.nav.NavDestinations
import com.example.madeinburundi.ui.theme.GreenMIB
import com.example.madeinburundi.viewmodel.AuthViewModel
import com.example.madeinburundi.viewmodel.UserViewModel


@Composable
fun AuthScreen(
modifier: Modifier = Modifier,
onBackClick: () -> Unit,
onLoginSuccess: () -> Unit,
navController: NavController,
viewModel: AuthViewModel = hiltViewModel(),
userViewModel: UserViewModel
) {
  var isLogin by rememberSaveable { mutableStateOf(true) }
  var fullName by rememberSaveable { mutableStateOf("") }
  var phone by rememberSaveable { mutableStateOf("") }
  var address by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  val loading by viewModel.loading.collectAsState()
  val message by viewModel.message.collectAsState()
  val isError by viewModel.isError.collectAsState()
  val token by viewModel.token.collectAsState()
  val loginSuccess by viewModel.loginSuccess.collectAsState()

  val context = LocalContext.current

  LaunchedEffect(loginSuccess) {
    if (loginSuccess) {
      userViewModel.loadUserProfile()
      //navController.popBackStack() // Go back to previous screen
    }
  }
  LaunchedEffect(token) {
    println("Token received: $token")
    if (token != null) {
      onLoginSuccess()
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp)
      .background(color = MaterialTheme.colorScheme.background )
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column (
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.Start
    ){
        IconButton(onClick = { navController.navigate(NavDestinations.HOME) }) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
        }
    }
    Spacer(modifier = Modifier.height(48.dp))
    Image(
      painter = painterResource(id = R.drawable.logo),
      contentDescription = "DUN",
      modifier = Modifier.size(96.dp),
      contentScale = ContentScale.Fit
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = if (isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_signin),
      style = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
      )
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column(
      modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp, horizontal = 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
      if(!isLogin) {
        ProfileTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = stringResource(R.string.f_name),
          leadingIconVector = Icons.Filled.Person,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        )
      }
      PhoneInputField(
        modifier = Modifier,
        onPhoneChanged = { phone = it},
        countries = countryList
        )
      ProfileTextField(
        value = password,
        onValueChange = { password = it },
        label = stringResource(R.string.f_pwd),
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
        placeholderText = stringResource(R.string.f_enter_pwd)
      )
    }
//    if(!isLogin){
//      Row(
//        modifier = Modifier.fillMaxWidth()
//          .padding(vertical = 0.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.End
//      ){
//        TextButton(onClick = {}){
//          Text(text = "Mot de passe oublié ?")
//        }
//      }
//    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        if (isLogin) {
          viewModel.login(phone, password)
        } else {
          viewModel.register(fullName, phone, password)
          isLogin = viewModel.registerOk.value
          fullName = ""
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    ) {
      Text(if (loading) stringResource(R.string.auth_wait) else if (isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_signin))
    }

    // Show message
    if (message.isNotBlank()) {
      SmallText(text = message, color = if (isError) MaterialTheme.colorScheme.onError  else MaterialTheme.colorScheme.primary)
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ){
      Text(text = if (isLogin) stringResource(R.string.auth_not_yet_user) else stringResource(R.string.auth_user_yet) )
      TextButton(onClick = { isLogin = !isLogin}){
        Text(text = if(isLogin) stringResource(R.string.auth_create_one) else stringResource(R.string.auth_login_here))
      }
    }
  }
}



data class Country(
  val name: String,
  val code: String,
  @DrawableRes val flag: Int,
  val initial: String
)

val countryList = listOf(
  Country("Burundi", "257", R.drawable.bi, "BDI"),
  Country("DRC", "243", R.drawable.cd, "RDC"),
  Country("Kenya", "254", R.drawable.ke, "KE"),
  Country("Ouganda", "256", R.drawable.ug, "UG"),
  Country("Rwanda", "250", R.drawable.rw, "RW"),
  Country("Tanzania", "255", R.drawable.tz, "TZ")
)