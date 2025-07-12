package com.example.madeinburundi.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.madeinburundi.ui.theme.FontSizes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SnackbarTool(
    isVisible: Boolean,
    resetState: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    message: String,
    topPadding: Dp
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
            }
            kotlinx.coroutines.delay(1200)
            snackbarHostState.currentSnackbarData?.dismiss()
            resetState()
        }
    }
    val currentSnackBarData = snackbarHostState.currentSnackbarData

    if (currentSnackBarData != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF303030),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentSnackBarData.visuals.message,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = FontSizes.caption(),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    IconButton(
                        onClick = { snackbarHostState.currentSnackbarData?.dismiss() },
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}


