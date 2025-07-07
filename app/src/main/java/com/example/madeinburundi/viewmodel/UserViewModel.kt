package com.example.madeinburundi.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.User
import com.example.madeinburundi.data.model.UserManager
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

  fun updateUser(fullname: String?, address: String?) {
    viewModelScope.launch {
      isLoading = true
      try {
        val success = userRepository.editUser(UserUpdate(fullname, address))
        if (success) {
          println("Mise a jour reussie")
        } else {
          println("Echec de la mise a jour")
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        isLoading = false
      }
    }
  }

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

  fun uploadImage() {
    val uri = selectedImageUri ?: return
    viewModelScope.launch {
      isUploading = true
      val success = userRepository.uploadImage(uri)
      isUploading = false
      isImagePicked = false
      uploadMessage = if (success) {
        loadUserProfile() // 👈 refresh profile after upload
        "Upload successful"
      } else {
        "Upload failed"
      }
    }
  }
}