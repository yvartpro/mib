package bi.vovota.madeinburundi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.UserManager
import bi.vovota.madeinburundi.ui.nav.EcommerceApp
import bi.vovota.madeinburundi.ui.theme.MadeInBurundiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    TokenManager.init(applicationContext)
    UserManager.init(applicationContext)
    setContent {
      MadeInBurundiTheme(darkTheme = false) {
        EcommerceApp()
      }
    }
  }
}