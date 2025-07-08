package com.example.madeinburundi.ui.screen

import CompanyShimmerCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.madeinburundi.data.model.Company
import com.example.madeinburundi.viewmodel.CompanyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CompanyScreen(
  navController: NavController,
  companies: List<Company>,
  companyViewModel: CompanyViewModel
) {
  var isLoading = companyViewModel.isLoading
  val scope = rememberCoroutineScope()
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {
 LaunchedEffect(Unit) {
   scope.launch {
     delay(5000)
     isLoading = false
   }
 }
    if(isLoading) {
      LazyColumn {
        items(4) { item ->
          CompanyShimmerCard()
        }
      }
    }else{
    if (companies.isEmpty()) {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Aucune industrie disponible")
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(companies, key = { it.id }) { company ->
          CompanyListItem(company = company, navController = navController)
        }
      }
    }}
  }
}


@Composable
fun CompanyListItem(company: Company, navController: NavController) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { navController.navigate("company/${company.id}") },
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // white
  ) {
    Row(
      modifier = Modifier
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        model = company.logo,
        contentDescription = "${company.name} logo",
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = company.name,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        company.slogan?.let {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = it,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }

        company.year?.let {
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Depuis $it",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
  }
}