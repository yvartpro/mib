package bi.vovota.madeinburundi.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bi.vovota.madeinburundi.R
import bi.vovota.madeinburundi.ui.screen.OrderState

@Composable
fun MyDropDownMenu(
options: List<OrderState>,
selectedOption: String?,
onOptionSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = options.find { it.value == selectedOption }?.title ?: stringResource(R.string.pr_filter)

    Box {
        Text(
            text = displayText,
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.pr_all)) },
                onClick = {
                    expanded = false
                    onOptionSelected(null)
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.title) },
                    onClick = {
                        expanded = false
                        onOptionSelected(option.value)
                    }
                )
            }
        }
    }
}
