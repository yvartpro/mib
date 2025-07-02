package com.example.madeinburundi.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madeinburundi.R
import com.example.madeinburundi.ui.theme.GreenMIB

@Composable
fun InputField(
  modifier: Modifier = Modifier,
  value: String,
  onValueChange: (String) -> Unit,
  labelText: String,
  keyboardType: KeyboardType = KeyboardType.Text,
  visualTransformation: VisualTransformation = VisualTransformation.None
){
  OutlinedTextField(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    label = {Text(text=labelText)},
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    shape = RoundedCornerShape(percent = 10),
    visualTransformation = visualTransformation,
    singleLine = true
  )
}

@Composable
fun PasswordField(
  modifier: Modifier,
  value: String,
  onValueChange: (String) -> Unit,
  labelText: String,
  keyboardType: KeyboardType = KeyboardType.Password,
  visualTransformation: VisualTransformation = VisualTransformation.None
){
  var isPasswordVisible by remember { mutableStateOf(false) }
  OutlinedTextField(
    modifier = modifier,
    value= value,
    onValueChange= onValueChange,
    label = {Text(text = labelText)},
    trailingIcon = {
      val icon = if (isPasswordVisible){
        painterResource(id = R.drawable.visibility)
      }else{
        painterResource(id = R.drawable.visibility_off)
      }
      IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
        Icon(painter = icon, contentDescription = null, tint = Color.Green)
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    visualTransformation = if (isPasswordVisible) visualTransformation else PasswordVisualTransformation() ,
    shape = RoundedCornerShape(percent = 10),
    singleLine = true
  )
}

@Composable
fun BasicInput(title: String, value: String){
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    Text(text = title, fontSize = 12.sp, color = Color.Gray)
    Box(
      modifier = Modifier
        .fillMaxSize()
        .height(48.dp)
        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
        .background(Color.White, RoundedCornerShape(4.dp))
        .padding(horizontal = 8.dp),
      contentAlignment = Alignment.Center
    ){
      BasicTextField(
        value = value,
        onValueChange = {},
        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
fun TextInput(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  keyboardType: KeyboardType = KeyboardType.Text
){
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    textStyle = TextStyle(fontSize = 14.sp),
    placeholder = { if (value.isEmpty()) Text(label) },
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    colors = TextFieldDefaults.colors(
      focusedIndicatorColor = MaterialTheme.colorScheme.primary,
      unfocusedIndicatorColor = Color.White,
      cursorColor = Color.Red
    ),
    modifier = Modifier
      .fillMaxWidth()
  )
}

@Composable
fun PasswordInput(
  password: String,
  onPasswordChange: (String) -> Unit,
  placeholderText: String = "Mot de passe",
  keyboardType: KeyboardType = KeyboardType.Password,
  modifier: Modifier = Modifier
) {
  var isVisible by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = password,
    onValueChange = onPasswordChange,
    modifier = modifier.fillMaxWidth(),
    singleLine = true,
    textStyle = TextStyle(fontSize = 14.sp),
    placeholder = { Text(placeholderText) },
    visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    trailingIcon = {
      val imagePainter = if (isVisible)
        painterResource(id = R.drawable.visibility) // Ensure this drawable exists
      else
        painterResource(id = R.drawable.visibility_off) // Ensure this drawable exists
      val description = if (isVisible) "Hide password" else "Show password"

      IconButton(onClick = { isVisible = !isVisible }) {
        Icon(
          painter = imagePainter,
          contentDescription = description,
          tint = GreenMIB
        )
      }
    },
    shape = RoundedCornerShape(6.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = MaterialTheme.colorScheme.primary,
      unfocusedBorderColor = Color.Gray,
      cursorColor = MaterialTheme.colorScheme.primary,
      // To make the placeholder text less prominent when unfocused (default behavior is usually good)
      // unfocusedPlaceholderColor = Color.LightGray, // Example
      // focusedPlaceholderColor = Color.DarkGray, // Example
    )
  )
}

@Composable
fun ProfileTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  leadingIconVector: ImageVector,
  modifier: Modifier = Modifier,
  keyboardType: KeyboardType = KeyboardType.Text,
  imeAction: ImeAction = ImeAction.Next,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  trailingIcon: @Composable (() -> Unit)? = null,
  isSensitive: Boolean = false,
  placeholderText: String? = null
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, style = MaterialTheme.typography.bodySmall) }, // Smaller label for text field
    leadingIcon = { Icon(leadingIconVector, contentDescription = label) },
    trailingIcon = trailingIcon,
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    singleLine = true,
    visualTransformation = visualTransformation,
    keyboardOptions = KeyboardOptions(
      keyboardType = keyboardType,
      imeAction = imeAction
    ),
    placeholder = placeholderText?.let { {Text(it, style = MaterialTheme.typography.bodySmall)} },
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = MaterialTheme.colorScheme.primary,
      unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    )
  )
}