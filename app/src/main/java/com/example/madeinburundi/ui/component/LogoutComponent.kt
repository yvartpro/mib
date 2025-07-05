package com.example.madeinburundi.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.madeinburundi.R
import com.example.madeinburundi.ui.theme.FontSizes

@Composable
fun LogoutButton(
    onClick: ()-> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout",
            modifier = modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = FontSizes.caption()
        )
    }
}