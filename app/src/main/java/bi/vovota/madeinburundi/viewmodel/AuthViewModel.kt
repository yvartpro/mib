package bi.vovota.madeinburundi.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.model.Country
import bi.vovota.madeinburundi.data.model.UserLogin
import bi.vovota.madeinburundi.data.model.countryList
import bi.vovota.madeinburundi.data.remote.dto.RegisterRequest
import bi.vovota.madeinburundi.data.remote.dto.RegisterResponse
import bi.vovota.madeinburundi.data.repository.AuthRepo
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.utils.UiState
import bi.vovota.madeinburundi.utils.launchWithState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val repo: AuthRepo
): ViewModel() {

  private val _authState = MutableStateFlow(AuthState.LOGGED_OUT)
  val authState = _authState.asStateFlow()
  private val _registerState = MutableStateFlow(UiState<RegisterResponse>())
  val registerState = _registerState.asStateFlow()
    private val _loginState = MutableStateFlow(UiState<TokenResponse>())
    val loginState = _loginState.asStateFlow()
  private val _registerOk = MutableStateFlow(false)
  val registerOk= _registerOk.asStateFlow()
  private val _pwdUnmatch = MutableStateFlow(false)
  val pwdUnmatch= _pwdUnmatch.asStateFlow()
    
  /**  input variables */
  private val _fullName = MutableStateFlow("")
  val fullName = _fullName.asStateFlow()
  private val _phone = MutableStateFlow("")
  val phone = _phone.asStateFlow()
  private val _isPhoneValid = MutableStateFlow(true)
  val isPhoneValid = _isPhoneValid.asStateFlow()
  private val _country = MutableStateFlow(countryList[0])
  val country = _country.asStateFlow()
  private val _password = MutableStateFlow("")
  val password = _password.asStateFlow()
  private val _passwordV = MutableStateFlow("")
  val passwordV = _passwordV.asStateFlow()

  /** input setters */
  fun setFullName(value: String) = _fullName.tryEmit(value)
  fun setPhone(value: String) = _phone.tryEmit(value)
  fun setPassword(value: String) = _password.tryEmit(value)
  fun setPasswordV(value: String) = _passwordV.tryEmit(value)
  fun setCountry(value: Country) = _country.tryEmit(value)

  init {
    checkAuthState()
  }
  private fun checkAuthState() {
    viewModelScope.launch {
      _authState.value = if (repo.getToken() != null) {
        AuthState.LOGGED_IN
      } else {
        AuthState.LOGGED_OUT
      }
    }
  }
    private fun verifyPhone() {
        _isPhoneValid.value = _phone.value.length == _country.value.numberLength
    }
  fun verifyPwd(pwd: String, pwdV: String) {
    _pwdUnmatch.value = pwd != pwdV
  }

  private val _loginSuccess = MutableStateFlow(false)
  val loginSuccess= _loginSuccess

  fun createUser() {
    verifyPhone()
    if (_fullName.value.isEmpty() || _password.value.isEmpty() || _passwordV.value.isEmpty()) {
      _registerState.value = UiState(error = "Veuillez remplir tous les champs")
      return
    }
    viewModelScope.launch {
        val phoneNumber = _country.value.code + _phone.value
      launchWithState(
        stateFlow = _registerState,
        block = { repo.register(RegisterRequest(_fullName.value, phoneNumber, _password.value))},
        onSuccess = { viewModelScope.launch { loginUser() }},
        onFailure = { e-> e.message?.let { Logger.e("Register failed: ", it)}}
      )
    }
  }

  fun loginUser() {
    verifyPhone()
    if (_password.value.isEmpty()) {
      _loginState.value = UiState(error = "Veuillez remplir tous les champs")
      return
    }

    viewModelScope.launch {
      val phoneNumber = _country.value.code + _phone.value
      launchWithState(
        stateFlow = _loginState,
        block = { repo.login(UserLogin(phoneNumber, _password.value)) },
        onSuccess = {
          _authState.value = AuthState.LOGGED_IN
          clearForm()
        }
      )
    }
  }

  fun logout() {
    viewModelScope.launch {
      repo.clearUser()
      _authState.value = AuthState.LOGGED_OUT
    }
  }

  private fun clearForm() {
    _fullName.value = ""
    _phone.value = ""
    _country.value = countryList[0]
    _password.value = ""
    _passwordV.value = ""
    _isPhoneValid.value = true
  }
}

enum class AuthState { LOGGED_IN, LOGGED_OUT, LOADING }