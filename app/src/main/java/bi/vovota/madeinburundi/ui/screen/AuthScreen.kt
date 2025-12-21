package bi.vovota.madeinburundi.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavController
import bi.vovota.madeinburundi.R
import bi.vovota.madeinburundi.data.model.Country
import bi.vovota.madeinburundi.data.model.countryList
import bi.vovota.madeinburundi.ui.component.ProfileTextField
import bi.vovota.madeinburundi.ui.component.SmallText
import bi.vovota.madeinburundi.ui.nav.NavDestinations
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.viewmodel.AuthState
import bi.vovota.madeinburundi.viewmodel.AuthViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AuthScreen(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
  navController: NavController,
  authViewModel: AuthViewModel,
) {
  var isLogin by rememberSaveable { mutableStateOf(true) }
  val fullName by authViewModel.fullName.collectAsState()
  val phone by authViewModel.phone.collectAsState()
  val password by authViewModel.password.collectAsState()
  val passwordV by authViewModel.passwordV.collectAsState()
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  val loginState by authViewModel.loginState.collectAsState()
  val registerState by authViewModel.registerState.collectAsState()
  val authState by authViewModel.authState.collectAsState()
  val isPhoneValid by authViewModel.isPhoneValid.collectAsState()

  LaunchedEffect(authState) {
    if (authState == AuthState.LOGGED_IN) {
      navController.navigate(NavDestinations.PROFILE) {
        popUpTo(NavDestinations.AUTH) { inclusive = true }
      }
    }
  }
  /** systemBackButton brings to Home not Profile */
  BackHandler(enabled = true) {
    navController.navigate(NavDestinations.HOME) {
      popUpTo(NavDestinations.AUTH) { inclusive = true }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(0.dp)
      .background(color = MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState())
      .imePadding(),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column (
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.Start
    ){
        IconButton(onClick = onBackClick ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
        }
    }
    Spacer(modifier = Modifier.height(48.dp))
    Image(
      painter = painterResource(id = R.drawable.logo),
      contentDescription = stringResource(R.string.app_name),
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
          onValueChange = { authViewModel.setFullName(it) },
          label = stringResource(R.string.f_name),
          leadingIconVector = Icons.Filled.Person,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next,
          isSensitive = false
        )
      }
      CountryDropdownWithFlags(
          isPhoneValid = isPhoneValid,
          onCountrySelected = { authViewModel.setCountry(it)},
          onPhoneNumberChanged = { authViewModel.setPhone(it) }
      )
      ProfileTextField(
        value = password,
        onValueChange = { authViewModel.setPassword(it) },
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
        imeAction = ImeAction.Next,
        isSensitive = true,
        placeholderText = stringResource(R.string.f_enter_pwd)
      )
      if(!isLogin) {
        ProfileTextField(
          value = passwordV,
          onValueChange = { authViewModel.setPasswordV(it) },
          label = stringResource(R.string.pwd_v),
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
          placeholderText = stringResource(R.string.pwd_v)
        )
        if (authViewModel.pwdUnmatch.value) {
          SmallText(text = stringResource(R.string.pwd_unmatch), color = MaterialTheme.colorScheme.error)
        }
      }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        if (isLogin) {
          authViewModel.loginUser()
        } else {
          Log.e("Register phone:", "${phone}${fullName}")
          authViewModel.verifyPwd(password, passwordV)
          if (!authViewModel.pwdUnmatch.value) {
            authViewModel.createUser()
            isLogin = authViewModel.registerOk.value
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    ) {
      Text(
          when {
              loginState.isLoading -> stringResource(R.string.auth_wait)
              registerState.isLoading -> stringResource(R.string.auth_wait)
              else -> if (isLogin) stringResource(R.string.auth_login) else stringResource(R.string.auth_signin)
          }
      )
    }

    // Show message and error
      when {
          registerState.data != null -> SmallText(text = stringResource(R.string.success_create_acc), color = MaterialTheme.colorScheme.primary)
          loginState.error != null -> SmallText(text = loginState.error!!, color = MaterialTheme.colorScheme.error)
          registerState.error != null -> SmallText(text = registerState.error!!, color = MaterialTheme.colorScheme.error)
          else -> {}
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


@Composable
fun CountryDropdownWithFlags(
    isPhoneValid: Boolean,
    onCountrySelected: (Country) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedCountry by remember { mutableStateOf(countryList[0]) }
  var phoneNumber by remember { mutableStateOf("") }

  OutlinedTextField(
    value = phoneNumber,
    onValueChange = {
      phoneNumber = it
      onPhoneNumberChanged(it)
    },
    label = { Text("Phone number") },
    isError = !isPhoneValid,
    leadingIcon = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .padding(start = 8.dp)
          .clickable { expanded = true }
      ) {
        Image(
          painter = painterResource(id = selectedCountry.flag),
          contentDescription = null,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
          imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
          contentDescription = null,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "+${selectedCountry.code}")
        Spacer(modifier = Modifier.width(4.dp))
      }
    },
    supportingText = {
      if (!isPhoneValid) {
        Text("Format attendu: ${selectedCountry.code} + ${selectedCountry.numberLength} chiffres")
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    modifier = Modifier.fillMaxWidth()
  )

  DropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false }
  ) {
    countryList.forEach { country ->
      DropdownMenuItem(
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
              painter = painterResource(id = country.flag),
              contentDescription = null,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${country.name} (+${country.code})")
          }
        },
        onClick = { 
            onCountrySelected(country)
            expanded = false
        }
      )
    }
  }
}