package com.example.madeinburundi.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.madeinburundi.viewmodel.UserViewModel
import io.ktor.websocket.Frame

@Composable
fun ImageUploadSection(viewModel: UserViewModel) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let(viewModel::onImageSelected)
    }

    val user = viewModel.user
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.selectedImageUri?.let {
            AsyncImage(model = it, contentDescription = null, modifier = Modifier.size(120.dp))
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Frame.Text("Choose Image")
        }

        Button(
            onClick = {
                viewModel.uploadImage(user?.id)
                println("Uploaded image: ${viewModel.user}")
                      },
            enabled = viewModel.selectedImageUri != null && !viewModel.isUploading
        ) {
            if (viewModel.isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Frame.Text("Upload")
        }

        viewModel.uploadMessage?.let {
            Text(it, color = if (it.contains("success")) Color.Green else Color.Red)
        }
    }
}
