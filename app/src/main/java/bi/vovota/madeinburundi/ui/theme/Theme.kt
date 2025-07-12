package bi.vovota.madeinburundi.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
  primary = GreenDark,
  onPrimary = Color(0xFF00390A),

  secondary = GoldAccentDark,
  onSecondary = Color(0xFF402D00),

  tertiary = CreamAccentDark,
  onTertiary = Color(0xFF3F2E24),

  background = DarkBackground,
  onBackground = LightText,

  onError = Color(0xFFEF5350),

  surface = DarkBackground,
  onSurface = LightText
)

private val LightColorScheme = lightColorScheme(
  primary = GreenMIB,
  onPrimary = Color.White,

  secondary = PurpleGrey40,
  onSecondary = Color.White,

  tertiary = PurpleGrey40,
  onTertiary = Color.White,

  background = LightBackground,
  onBackground = DarkText,

  onError = Color.Red,

  surface = LightBackground,
  onSurface = DarkText
)


@Composable
fun MadeInBurundiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = Color.Transparent.toArgb()

      // Use WindowCompat for setting status bar icon color
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
        colorScheme.background.luminance() > 0.5f // true for dark icons, false for light icons
    }
  }
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
