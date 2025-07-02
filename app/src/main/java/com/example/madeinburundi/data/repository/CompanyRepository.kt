package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.model.Company
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CompanyRepository @Inject constructor(
  private val client: HttpClient
) {
  suspend fun getCompanies(): List<Company> {
    return try {
      val response: HttpResponse = client.get("https://mib.vovota.bi/api/company/")
      val rawJson = response.bodyAsText()

      val companies = Json.decodeFromString<List<Company>>(rawJson)
      companies
    } catch (e: Exception) {
      println("❌ Exception: ${e.message}")
      emptyList()
    }
  }
}