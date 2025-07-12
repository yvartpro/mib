package bi.vovota.madeinburundi.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bi.vovota.madeinburundi.viewmodel.UserViewModel

@Composable
fun ProfileImageUploader(
    userViewModel: UserViewModel,
    userId: Int
) {
    val imageUri = userViewModel.selectedImageUri
    val isUploading = userViewModel.isUploading
    val uploadMessage = userViewModel.uploadMessage
    println("$isUploading $uploadMessage $imageUri")

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let(userViewModel::onImageSelected)
    }

    Column(modifier = Modifier.padding(all = 16.dp)) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Choisir une image")
        }

//        imageUri?.let {
//            Spacer(Modifier.height(12.dp))
//
//            AsyncImage(
//                model = it,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
//
//            Spacer(Modifier.height(8.dp))
//
//            Button(
//                onClick = { userViewModel.uploadImage(userId) },
//                enabled = !isUploading
//            ) {
//                println("Uploader clicked")
//                if (isUploading)
//                    CircularProgressIndicator(Modifier.size(18.dp))
//                else
//                    Text("Uploader")
//            }
//        }

//        uploadMessage?.let {
//            Text(
//                it,
//                color = if (it.startsWith("✅")) Color.Green else Color.Red,
//                fontSize = 14.sp,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
    }
}
