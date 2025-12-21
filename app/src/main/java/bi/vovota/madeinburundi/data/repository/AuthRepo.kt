package bi.vovota.madeinburundi.data.repository

import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.model.UserLogin
import bi.vovota.madeinburundi.data.remote.ApiService
import bi.vovota.madeinburundi.data.remote.dto.RegisterRequest
import bi.vovota.madeinburundi.data.remote.dto.RegisterResponse
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.utils.safeApiCall
import javax.inject.Inject


interface AuthRepo {
  suspend fun register(request: RegisterRequest): Result<RegisterResponse>
    suspend fun login(request: UserLogin): Result<TokenResponse>
    suspend fun getToken(): String?
    suspend fun getUser(): User?
    suspend fun clearUser()
}
class AuthRepoImpl @Inject constructor(
  private val api: ApiService,
  private val tokenManager: TokenManager
) : AuthRepo {

  override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
    return safeApiCall { api.register(request) }
  }

    override suspend fun login(request: UserLogin): Result<TokenResponse> {
      val result = safeApiCall { api.login(request)}
      result.getOrNull()?.let { tokens ->
        Logger.d("token", tokens.access)
        tokenManager.saveTokens(tokens.access, tokens.refresh)
        val user = api.getProfile(tokens.access)
        tokenManager.saveUser(user)
      }
      return result
    }

    override suspend fun getToken(): String? {
        return tokenManager.getAccessToken()
    }

    override suspend fun getUser(): User? {
        return tokenManager.getUser()
    }

    override suspend fun clearUser() {
        tokenManager.clearAll()
    }
}