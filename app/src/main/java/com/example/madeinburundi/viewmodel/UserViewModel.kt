package com.example.madeinburundi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madeinburundi.data.model.User
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

}