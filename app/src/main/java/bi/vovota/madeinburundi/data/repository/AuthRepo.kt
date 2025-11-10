package bi.vovota.madeinburundi.data.repository

import bi.vovota.madeinburundi.data.remote.ApiService
import bi.vovota.madeinburundi.data.remote.dto.UserRegister
import bi.vovota.madeinburundi.utils.safeApiCall
import javax.inject.Inject


interface AuthRepo {
  suspend fun register(request: UserRegister): Result<Boolean>
}
class AuthRepoImpl @Inject constructor(
  private val api: ApiService
) : AuthRepo {

  override suspend fun register(request: UserRegister): Result<Boolean> {
    return safeApiCall { api.register(request) }
  }
}

