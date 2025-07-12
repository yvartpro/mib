package bi.vovota.madeinburundi.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun SmallText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontWeight: FontWeight? = null,
  textDecoration: TextDecoration? = null
) {
  Text(
    text = text,
    fontSize = 12.sp,
    color = color,
    fontWeight = fontWeight,
    textDecoration = textDecoration,
    modifier = modifier
  )
}