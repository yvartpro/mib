package com.example.madeinburundi.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun HeadingText(value: String, modifier: Modifier) {
  Text(
    text = value,
    fontWeight = FontWeight.Bold,
    style = MaterialTheme.typography.headlineSmall
  )
}

@Composable
fun NormalText(value: String) {
  Text(
    text = value,
    fontWeight = FontWeight.Medium,
    style = MaterialTheme.typography.labelLarge
  )
}

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