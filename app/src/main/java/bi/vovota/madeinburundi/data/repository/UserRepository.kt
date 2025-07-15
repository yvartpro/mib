package bi.vovota.madeinburundi.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.User
import bi.vovota.madeinburundi.data.model.UserRaw
import bi.vovota.madeinburundi.data.model.UserUpdate
import bi.vovota.madeinburundi.data.model.toUser
import bi.vovota.madeinburundi.data.model.TokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import io.ktor.client.request.*
import io.ktor.http.*


class UserRepository @Inject constructor(
  private val client: HttpClient,
  @ApplicationContext private val context: Context,
  private val tokenManager:
  TokenManager
)
{
  var onLoginNeeded: (()-> Unit)? = null
  suspend fun getProfile(): User {
    val token = tokenManager.getAccessToken()
    if (token == null) {
      Log.e("UserRepo", "Missing token")
      throw UnAuthorizedException("No access token found")
    }
    try {
      val response = client.get("https://mib.vovota.bi/api/profile/"){
          header(HttpHeaders.Authorization, "Bearer $token")
      }
      val responseText = response.bodyAsText()
      println("User list: $responseText")
      val users: List<UserRaw> = response.body()
      val user = users.firstOrNull()?.toUser() ?: throw Exception("Empty user list from API: $responseText")
      return  user
    } catch (e: ClientRequestException) {
      if (e.response.status == HttpStatusCode.Unauthorized) {
        println("Access token expired. Trying refreshing...")
        val refreshed = refreshToken()
        if (refreshed) {
          val newAccess = TokenManager.getAccessToken()
          val retryResponse =  client.get("https://mib.vovota.bi/api/profile/"){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $newAccess")
          }
          return retryResponse.body()
        } else {
          throw UnAuthorizedException("Token refresh failed")
        }
      }else {
        throw e
      }
    }
  }



  suspend fun editUser(update: UserUpdate, userId: Int): Boolean {
    val access = TokenManager.getAccessToken() ?: throw UnAuthorizedException("No access token")
    return try {
      println("payload: $update")
      val response = client.patch("https://mib.vovota.bi/api/profile/$userId/"){
        header(HttpHeaders.Authorization, "Bearer $access")
        contentType(ContentType.Application.Json)
        setBody(update)
      }
      if ( response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Accepted) {
        println("Update repo: ${response.status}")
        true
      }else{
        println("Update repo: $response")
        false
      }
    } catch (e: ClientRequestException) {
      println("Client error: ${e.response.status}")
      false
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }


  private suspend fun refreshToken(): Boolean {
    val refresh = TokenManager.getRefreshToken()
    if (refresh.isNullOrEmpty()) {
      println("No refresh token available")
      return false
    }
    return try {
      val response = client.post("https://mib.vovota.bi/api/refresh/") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("refresh" to refresh))
      }
      val tokenResponse = response.body<TokenResponse>()
      println("Token refreshed: ${tokenResponse.access}")
      TokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
      true
    } catch (e: Exception) {
      println("Refresh failed: ${e.message}")
      TokenManager.clearTokens()
      false
    }
  }
}

class UnAuthorizedException(message: String): Exception(message)