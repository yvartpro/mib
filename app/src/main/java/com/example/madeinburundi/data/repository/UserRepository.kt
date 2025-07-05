package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.AuthRepository
import com.example.madeinburundi.data.model.Company
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.data.model.TokenResponse
import com.example.madeinburundi.data.model.User
import com.example.madeinburundi.data.model.UserRaw
import com.example.madeinburundi.data.model.UserWrapper
import com.example.madeinburundi.data.model.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
  private val client: HttpClient
) {
  var onLoginNeeded: (()-> Unit)? = null
  suspend fun getProfile(): User {
    val access = TokenManager.getAccessToken()
    println("Passed access_token: $access")
    val token = access ?: throw UnAuthorizedException("No access token found")
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

  private suspend fun refreshToken(): Boolean {
    val refresh = TokenManager.getRefreshToken()
    if (refresh.isNullOrEmpty()) {
      println("No refresh token available")
      return false
    }
    return try {
      val response = client.post("https://mib.vovota.bi/api/refresh") {
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