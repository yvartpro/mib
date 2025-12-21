package bi.vovota.madeinburundi.data.repository

import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.model.UserLogin
import bi.vovota.madeinburundi.data.remote.ApiService
import bi.vovota.madeinburundi.data.remote.dto.UserRegister
import bi.vovota.madeinburundi.utils.Logger
import bi.vovota.madeinburundi.utils.decodeJwtToken
import bi.vovota.madeinburundi.utils.safeApiCall
import javax.inject.Inject


interface AuthRepo {
  suspend fun register(request: UserRegister): Result<Boolean>
    suspend fun login(request: UserLogin): Result<TokenResponse>
    suspend fun getToken(): String?
    suspend fun getUser(): User?
    suspend fun clearUser()
}
class AuthRepoImpl @Inject constructor(
  private val api: ApiService,
  private val tokenManager: TokenManager
) : AuthRepo {

  override suspend fun register(request: UserRegister): Result<Boolean> {
    return safeApiCall { api.register(request) }
  }

    override suspend fun login(request: UserLogin): Result<TokenResponse> {
        val result = safeApiCall { api.login(request)}
        result.getOrNull()?.let { tokens ->
            val user = decodeJwtToken(tokens.access)
            tokenManager.saveTokens(tokens.access, tokens.refresh)
//            tokenManager.saveUser(d.user)
            Logger.d("Logins", user.toString())
            Logger.d("Logins", tokens.toString())
        }
        return result
    }


    override suspend fun getToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun clearUser() {
        TODO("Not yet implemented")
    }
}

