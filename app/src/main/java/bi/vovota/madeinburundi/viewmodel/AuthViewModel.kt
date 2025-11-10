package bi.vovota.madeinburundi.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.repository.UserRepository
import bi.vovota.madeinburundi.data.AuthRepository
import bi.vovota.madeinburundi.data.remote.dto.UserRegister
import bi.vovota.madeinburundi.data.repository.AuthRepo
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.utils.UiState
import bi.vovota.madeinburundi.utils.launchWithState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val authRepository: AuthRepository,
  private val userRepository: UserRepository,
  private val repo: AuthRepo
): ViewModel() {

  private val _authState = MutableStateFlow(UiState<Boolean>())
  val authState = _authState.asStateFlow()
  private val _loading = MutableStateFlow(false)
  val loading: StateFlow<Boolean> = _loading

  private val _registerOk = MutableStateFlow(false)
  val registerOk: StateFlow<Boolean> = _registerOk

  private val _message = MutableStateFlow("")
  val message: StateFlow<String> = _message

  private val _isError = MutableStateFlow(false)
  val isError: StateFlow<Boolean> = _isError

  private val _token = MutableStateFlow<TokenResponse?>(null)
  val token: StateFlow<TokenResponse?> = _token

  private val _pwdUnmatch = MutableStateFlow(false)
  val pwdUnmatch: StateFlow<Boolean> = _pwdUnmatch

  fun verifyPwd(pwd: String, pwdV: String) {
    _pwdUnmatch.value = pwd != pwdV
  }

  private val _loginSuccess = MutableStateFlow(false)
  val loginSuccess: StateFlow<Boolean> = _loginSuccess

  fun createUser(request: UserRegister) {
    viewModelScope.launch {
      launchWithState(
        stateFlow = _authState,
        block = { repo.register(request)},
        onSuccess = {
          Logger.d("Register", "Success")
        }, onFailure = { e-> e.message?.let { Logger.e("Register", it)}}
      )
    }
  }

  fun register(fullName: String, phone: String, password: String) {
    _loading.value = true
    _isError.value = false
    viewModelScope.launch {
      try {
        val result = authRepository.registerUser(fullName, phone, password)
        if (result) {
          _message.value = "Compte créé avec succès"
          _registerOk.value = true
        } else {
          _isError.value = true
          _message.value = "Une erreur est survenue"
        }
      } catch (e: Exception) {
        _isError.value = true
        _message.value = "Une erreur est survenue"
      } finally {
        delay(1500)
        _loading.value = false
      }
    }
  }

  fun login(phone: String, password: String) {
    _loading.value = true
    _isError.value = false
    viewModelScope.launch {
      try {
        val result = authRepository.loginUser(phone, password)
        if (result != null) {
          _token.value = result
          TokenManager.saveTokens(result.access, result.refresh)
          _loginSuccess.value = true
          _message.value = "Connexion réussie"
        } else {
          _isError.value = true
          _message.value = "Échec de la connexion"
        }
      } catch (e: Exception) {
        _isError.value = true
        _message.value = "Erreur serveur"
      } finally {
        _loading.value = false
      }
    }
  }
}
