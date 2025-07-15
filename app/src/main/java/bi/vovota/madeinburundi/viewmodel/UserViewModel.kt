package bi.vovota.madeinburundi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.CartItem
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.model.UserUpdate
import bi.vovota.madeinburundi.data.repository.UnAuthorizedException
import bi.vovota.madeinburundi.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel  @Inject constructor(private val userRepository: UserRepository): ViewModel(){
  var user by mutableStateOf<User?>(null)
    private set
  var isLoading by mutableStateOf(false)
    private set
  var error by mutableStateOf<String?>(null)
    private set

  var isEditMode by mutableStateOf(false)
    private set

  fun toggleEdit() {
    isEditMode = !isEditMode
  }

  private val _navigateToLogin = Channel<Unit>(Channel.CONFLATED)
  val navigateToLogin = _navigateToLogin.receiveAsFlow()

  fun loadUserProfile() {
    viewModelScope.launch {
      isLoading = true
      error = null
      try {
        user = userRepository.getProfile()
        println("User is: $user")
      }catch (e: UnAuthorizedException){
        _navigateToLogin.trySend(Unit)
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
  var isUpdating by mutableStateOf(false)
    private set
  fun updateUser(update: UserUpdate, userId: Int) {
    viewModelScope.launch {
      isUpdating = true
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
        isUpdating = false
      }
    }
  }

  fun getCartPrice(user: User?, product: Product): Double {
    return when (user?.code) {
      "254" -> product.kePrice
      "255" -> product.tzPrice
      "250" -> product.rwPrice
      "243" -> product.drcPrice
      "256" -> product.ugPrice
      else ->  product.bdiPrice
    }.toDoubleOrNull() ?: 0.0
  }

}


