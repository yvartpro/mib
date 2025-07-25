package bi.vovota.madeinburundi.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import bi.vovota.madeinburundi.ui.screen.Country
import bi.vovota.madeinburundi.ui.theme.FontSizes

@Composable
fun ProfileTextField(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  isSensitive: Boolean,
  leadingIconVector: ImageVector,
  modifier: Modifier = Modifier,
  keyboardType: KeyboardType = KeyboardType.Text,
  imeAction: ImeAction = ImeAction.Next,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  trailingIcon: @Composable (() -> Unit)? = null,
  placeholderText: String? = null
) {
  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(label, style = MaterialTheme.typography.bodySmall) },
    leadingIcon = { Icon(leadingIconVector, contentDescription = label) },
    trailingIcon = trailingIcon,
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneInputField(
  modifier: Modifier = Modifier,
  onPhoneChanged: (String) -> Unit,
  imeAction: ImeAction = ImeAction.Next,
  countries: List<Country>
) {
  var expanded by remember { mutableStateOf(false) }
  var selectedCountry by remember { mutableStateOf(countries.first()) }
  var localNumber by remember { mutableStateOf("") }

  val fullNumber = "${selectedCountry.code}$localNumber"
  val code = selectedCountry.code
  LaunchedEffect(fullNumber) {
    onPhoneChanged(fullNumber)
  }

  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,

  ) {
    // Country Code Dropdown
    ExposedDropdownMenuBox(
      expanded = expanded,
      onExpandedChange = { expanded = !expanded }
    ) {
      OutlinedTextField(
        readOnly = true,
        value = selectedCountry.initial,
        onValueChange = { println(selectedCountry)},
        trailingIcon = {
          ExposedDropdownMenuDefaults.TrailingIcon(expanded)
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = imeAction),
        textStyle = TextStyle(fontSize = FontSizes.body()),
        modifier = Modifier
          .menuAnchor()
          .width(100.dp)
          .fillMaxHeight()
      )

      ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
      ) {
        countries.forEach { country ->
          DropdownMenuItem(
            text = { Text("${country.code}")},
            leadingIcon = {
              Image(
                painter = painterResource(country.flag),
                contentDescription = country.name,
                modifier = Modifier.size(24.dp)
              )
            },
            onClick = {
              selectedCountry = country
              expanded = false
              localNumber = ""
            }
          )
        }
      }
    }
    Spacer(modifier = Modifier.width(8.dp))
    OutlinedTextField(
      value = localNumber,
      onValueChange = {
        localNumber = it.filter { c -> c.isDigit() }
      },
      textStyle = TextStyle(fontSize = FontSizes.body()),
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = imeAction),
      visualTransformation = PhonePrefixTransform("+${code}"),
      modifier = Modifier.weight(1f),
    )
  }
}


class PhonePrefixTransform(private val prefix: String): VisualTransformation {
  override fun filter(text: AnnotatedString): TransformedText {
    val newText = prefix + text.text
    val offSetMapping = object: OffsetMapping {
      override fun originalToTransformed(offset: Int): Int = offset + prefix.length
      override fun transformedToOriginal(offset: Int): Int = (offset - prefix.length).coerceAtLeast(0)
    }
    return TransformedText(AnnotatedString(newText), offSetMapping)
  }
}
