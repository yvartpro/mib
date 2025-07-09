package com.example.madeinburundi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.User
import com.example.madeinburundi.data.model.UserUpdate
import com.example.madeinburundi.data.repository.UnAuthorizedException
import com.example.madeinburundi.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel  @Inject constructor(private val userRepository: UserRepository): ViewModel(){
  var user by mutableStateOf<User?>(null)
    private set
  var isLoading by mutableStateOf(false)
    private set
  var error by mutableStateOf<String?>(null)
    private set

  private val _navigateToLogin = MutableSharedFlow<Unit>()
  val navigateToLogin = _navigateToLogin.asSharedFlow()

  fun loadUserProfile() {
    viewModelScope.launch {
      isLoading = true
      error = null
      try {
        user = userRepository.getProfile()
        println("User is: $user")
      }catch (e: UnAuthorizedException){
        _navigateToLogin.emit(Unit)
      }catch (e: Exception) {
        error = e.message
      }finally {
        isLoading = false
      }
    }
  }

  fun clearUser() {
    user = null
  }

  var updateMsg by mutableStateOf("")
    private set
  fun updateUser(update: UserUpdate, userId: Int) {
    viewModelScope.launch {
      isLoading = true
      try {
        val success = userRepository.editUser(update, userId)
        if (success) {
          updateMsg = "Mise a jour reussie"
          println("Mise a jour reussie $update")
        } else {
          updateMsg = "Echec de la mise a jour"
          println("Echec de la mise a jour, $update")
        }
      } catch (e: Exception) {
        updateMsg = e.message.toString()
        e.printStackTrace()
      } finally {
        isLoading = false
      }
    }
  }


  fun uploadImage(userId: Int, context: Context) {
    selectedImageUri?.let { uri ->
      viewModelScope.launch {
        isUploading = true
        val success = userRepository.uploadProfileImage(uri, userId, context)
        uploadMessage = if (success) "Image upload réussie" else "Échec de l'upload"
        isUploading = false
      }
    }
  }
//  fun uploadImage(userId: Int) {
//    println("$userId")
//    selectedImageUri?.let { uri ->
//      println("$uri")
//      viewModelScope.launch {
//        isUploading = true
//        val success = userRepository.uploadProfileImage(uri, userId)
//        println("$success")
//        uploadMessage = if (success) "Succès" else "Erreur"
//        isUploading = false
//      }
//    } ?: run { println("No image selected")}
//  }
  var selectedImageUri by mutableStateOf<Uri?>(null)
    private set

  var isUploading by mutableStateOf(false)
    private set

  var uploadMessage by mutableStateOf<String?>(null)
    private set

  var isImagePicked by mutableStateOf(false)
    private set

  fun onImageSelected(uri: Uri) {
    selectedImageUri = uri
    uploadMessage = null
    isImagePicked = true
  }
}


