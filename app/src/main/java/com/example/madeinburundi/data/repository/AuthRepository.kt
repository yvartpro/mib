package com.example.madeinburundi.data

import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.data.model.TokenResponse
import com.example.madeinburundi.data.model.UserLogin
import com.example.madeinburundi.data.model.UserRegister
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
  private val client: HttpClient
) {
  suspend fun registerUser(fullName: String, phoneNumber: String, password: String): Boolean {
    return try {
      val resp = client.post("https://mib.vovota.bi/api/register/") {
        contentType(ContentType.Application.Json)
        setBody(UserRegister(fullName, phoneNumber,  password))
      }
      if(resp.status == HttpStatusCode.Created || resp.status == HttpStatusCode.OK){
        println("Success register: ${resp.status}")
        true
      }else{
        val err = resp.bodyAsText()
        println("$fullName, $phoneNumber")
        println("Error register: ${resp.status} -$err ")
        false
      }
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  suspend fun loginUser(phoneNumber: String, password: String): TokenResponse? {
    return try {
      val resp = client.post("https://mib.vovota.bi/api/token/") {
        contentType(ContentType.Application.Json)
        setBody(UserLogin(phoneNumber, password))
      }
      resp.body()
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  suspend fun refreshToken(): Boolean {
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
