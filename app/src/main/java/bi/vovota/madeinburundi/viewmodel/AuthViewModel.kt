package bi.vovota.madeinburundi.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.repository.UserRepository
import bi.vovota.madeinburundi.data.AuthRepository
import bi.vovota.madeinburundi.data.model.Country
import bi.vovota.madeinburundi.data.model.UserLogin
import bi.vovota.madeinburundi.data.model.countryList
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
    private val _loginState = MutableStateFlow(UiState<TokenResponse>())
    val loginState = _loginState.asStateFlow()
  private val _loading = MutableStateFlow(false)
  val loading= _loading.asStateFlow()

  private val _registerOk = MutableStateFlow(false)
  val registerOk= _registerOk.asStateFlow()

  private val _message = MutableStateFlow("")
  val message: StateFlow<String> = _message.asStateFlow()

  private val _isError = MutableStateFlow(false)
  val isError= _isError.asStateFlow()

  private val _token = MutableStateFlow<TokenResponse?>(null)

  private val _pwdUnmatch = MutableStateFlow(false)
  val pwdUnmatch= _pwdUnmatch.asStateFlow()
    
    /**  input variables */
    private val _fullName = MutableStateFlow("")
    val fullName = _fullName.asStateFlow()
    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()
    private val _isPhoneValid = MutableStateFlow(false)
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

    private fun verifyPhone() {
        _isPhoneValid.value = _phone.value.length == _country.value.numberLength
    }
  fun verifyPwd(pwd: String, pwdV: String) {
    _pwdUnmatch.value = pwd != pwdV
  }

  private val _loginSuccess = MutableStateFlow(false)
  val loginSuccess= _loginSuccess

  fun createUser() {
      Logger.d("Register", "createUser")
      verifyPhone()
      if (_fullName.value.isEmpty() || _password.value.isEmpty() || _passwordV.value.isEmpty()) {
          _authState.value = UiState(error = "Veuillez remplir tous les champs")
          return
      }
    viewModelScope.launch {
        val phoneNumber = _country.value.code + _phone.value
      launchWithState(
        stateFlow = _authState,
        block = { repo.register(UserRegister(_fullName.value, phoneNumber, _password.value))},
        onSuccess = {
          Logger.d("Register", "Success")
        }, onFailure = { e-> e.message?.let { Logger.e("Register", it)}}
      )
    }
  }

    fun loginUser() {
        verifyPhone()
        if (_password.value.isEmpty()) {
            _authState.value = UiState(error = "Veuillez remplir tous les champs")
            return
        }
        viewModelScope.launch {
            val phoneNumber = _country.value.code + _phone.value
            launchWithState(
                stateFlow = _loginState,
                block = { repo.login(UserLogin(phoneNumber, _password.value))},
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
