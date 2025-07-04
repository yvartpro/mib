package com.example.madeinburundi.data

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
        setBody(UserRegister(fullName, phoneNumber, password))
      }
      println("Response: $resp")

      resp.status == HttpStatusCode.Created || resp.status == HttpStatusCode.OK
    } catch (e: Exception) {
      e.printStackTrace()
      println("Sapor: ${e.message}")
      false
    }
  }

  suspend fun loginUser(phoneNumber: String, password: String): TokenResponse? {
    return try {
      val resp = client.post("https://mib.vovota.bi/api/token/") {
        contentType(ContentType.Application.Json)
        setBody(UserLogin(phoneNumber, password))
      }
      if (resp.status.isSuccess()) {
        resp.body()
      } else {
        val errorText = resp.bodyAsText()
        println("Login error response: $errorText")
        null
      }
    } catch (e: Exception) {
      e.printStackTrace()
      println("Error 2: $e.message")
      null
    }
  }
}
