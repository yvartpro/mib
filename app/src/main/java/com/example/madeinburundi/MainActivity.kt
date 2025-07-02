package com.example.madeinburundi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.ui.nav.EcommerceApp
import com.example.madeinburundi.ui.theme.MadeInBurundiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    TokenManager.init(applicationContext)
    setContent {
      MadeInBurundiTheme {
        EcommerceApp()
      }
    }
  }
}