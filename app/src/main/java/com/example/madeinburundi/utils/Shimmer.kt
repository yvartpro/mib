deimport androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.madeinburundi.R
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.ui.theme.FontSizes
import com.example.madeinburundi.ui.theme.Spacings
import com.example.madeinburundi.viewmodel.CartViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import io.ktor.utils.io.concurrent.shared

@Composable
fun CompanyShimmerCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Simulated image section
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(CircleShape)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Simulated subtitle
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(12.dp)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Simulated subtitle
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(12.dp)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
            }
        }
    }
}


@Composable
fun ProductImageShimmerItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            tonalElevation = 1.dp,
            modifier = Modifier
                .size(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(10.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
    }
}


@Composable
fun RecommendedShimmerItem() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
            .padding(horizontal = Spacings.medium())
            .width(180.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
                .placeholder(
                    visible = true,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(10.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(12.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        }
    }
}

@Composable
fun ProductCardShimmer(modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {
        Column(
            modifier = Modifier.padding(Spacings.medium())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(140.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(12.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .width(28.dp)
                        .clip(CircleShape)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer()
                        ),
                )
            }
        }
    }
}


@Composable
fun ProfileShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(30.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )

        Spacer(modifier = Modifier.height(24.dp))

        repeat(4) {
            ProfileItemShimmer()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileItemShimmer() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    shape = CircleShape,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(12.dp)
                    .placeholder(
                        visible = true,
                        color = Color.Gray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .placeholder(
                        visible = true,
                        color = Color.Gray,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
        }
    }
}
