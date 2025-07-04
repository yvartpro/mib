package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.model.Company
import com.example.madeinburundi.data.model.TokenManager
import com.example.madeinburundi.data.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
  private val client: HttpClient
) {
  var onLoginNeeded: (()-> Unit)? = null
  suspend fun getProfile(onLoginNeeded: (() -> Unit)? = null): User {
    val token = TokenManager.getAccessToken() ?: throw UnAuthorizedException("No access token found")
    UnAuthorizedException("No access token found").printErrMessage()
    try {
      return client.get("https://mib.vovota.bi/api/profile/"){
          header(HttpHeaders.Authorization, "Bearer $token")
      }.body()
    } catch (e: ClientRequestException) {
      if (e.response.status == HttpStatusCode.Unauthorized) {
        throw UnAuthorizedException("Token expired or invalid")
        UnAuthorizedException("Expired or invalid token").printErrMessage()
      }else {
        throw e
      }
    }
  }
}

class UnAuthorizedException(message: String): Exception(message) {
  fun printErrMessage(){
    println("UnAuthErr: $message")
  }
}