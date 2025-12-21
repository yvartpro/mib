package bi.vovota.madeinburundi.data.remote

import bi.vovota.madeinburundi.data.model.TokenResponse
import bi.vovota.madeinburundi.data.model.UserLogin
import bi.vovota.madeinburundi.data.remote.dto.UserRegister
import bi.vovota.madeinburundi.utils.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*

class ApiService(
  private val client: HttpClient
) {
  private val baseUrl = "https://mib.clubtechlac.bi"

  suspend fun register(request: UserRegister): Boolean {
    val resp = client.post("$baseUrl/api/register/") {
      contentType(ContentType.Application.Json)
      setBody(request)
    }
    val txt = resp.bodyAsText()
    Logger.d("API SERVICE", txt)

    if (resp.status.isSuccess()) {
      return resp.body()
    } else {
      throw ClientRequestException(resp,txt)
    }
  }
    suspend fun login(request: UserLogin): TokenResponse {
        val resp = client.post("$baseUrl/token/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val txt = resp.bodyAsText()

        if (resp.status.isSuccess()) {
            return resp.body()
        } else {
            throw ClientRequestException(resp, txt)
        }
    }
}
