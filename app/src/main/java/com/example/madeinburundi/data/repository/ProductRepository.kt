package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.model.Product
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ProductRepository @Inject constructor(
  private val client: HttpClient
) {
  suspend fun getProducts(): List<Product> {
    return try {
      println("🔎 Calling Product API...")
      val response: HttpResponse = client.get("https://mib.vovota.bi/api/product/")
      println("✅ Response: ${response.status}")
      val rawJson = response.bodyAsText()
      println("📦 Body: $rawJson")

      val products = Json.decodeFromString<List<Product>>(rawJson)
      println("✅ Products loaded: ${products.size}")
      products
    } catch (e: Exception) {
      println("❌ Exception: ${e.message}")
      emptyList()
    }
  }
}
