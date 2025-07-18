package bi.vovota.madeinburundi.ui.component

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bi.vovota.madeinburundi.ui.theme.FontSizes
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

    snackbarHostState.currentSnackbarData?.let { snackbarData ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .zIndex(100f),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .wrapContentWidth()
                    .zIndex(101f)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = FontSizes.caption(),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = { snackbarData.dismiss() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


