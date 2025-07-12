package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.model.CartItem
import com.example.madeinburundi.data.model.NewOrder
import com.example.madeinburundi.data.model.Order
import com.example.madeinburundi.data.model.Product
import com.example.madeinburundi.data.model.TokenManager
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OrderRepository @Inject constructor(
  private val client: HttpClient
) {
  init {
    println("Received: ${client.hashCode()}")
  }
  suspend fun placeOrder(order: NewOrder): Boolean {
    val access = TokenManager.getAccessToken()
    println("Passed access_token: $access")
    val token = access ?: println("No token found") //throw UnAuthorizedException("No access token found")
    println("Sending...: ${order}")
    return try {
      val response = client.post("https://mib.vovota.bi/api/order/") {
        contentType(ContentType.Application.Json)
        header(HttpHeaders.Authorization, "Bearer $token")
        setBody(order)
      }
      println("Response: $response")
      response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK
    } catch (e: Exception) {
      e.printStackTrace()
      println("Error found: ${e.message}")
      false
    }finally {
      println("Order placed successfully or failed")
    }
  }
  suspend fun placeOrderMessage(customerId: Int, cartItems: List<CartItem>): Boolean {
    val description = buildOrderDescription(customerId, cartItems)
    return placeOrder(NewOrder(customerId, description))
  }

  private fun buildOrderDescription(customerId: Int, items: List<CartItem>): String {
    val total = items.sumOf { it.product.price.toDouble() * it.quantity.value }
    return buildString {
      append("Commande de l'utilisateur $customerId:\n")
      items.forEach {
        val sub = it.product.price.toDouble() * it.quantity.value
        append("- ${it.quantity.value}x ${it.product.name} (${it.product.price} FC): ${sub.toInt()} FC\n")
      }
      append("\nTotal: ${total.toInt()} FC")
    }
  }
  suspend fun getOrders(): List<Order> {
    val access = TokenManager.getAccessToken()
    println("Passed access_token: $access")
    val token = access ?: println("No token found")
    return try {
      println("🔎 Calling Product API...")
      val response: HttpResponse = client.get("https://mib.vovota.bi/api/order/") {
        contentType(ContentType.Application.Json)
        header(HttpHeaders.Authorization, "Bearer $token")
      }
      println("✅ Response: ${response.status}")
      val rawJson = response.bodyAsText()
      println("📦 Body: $rawJson")

      val orders = Json.decodeFromString<List<Order>>(rawJson)
      println("✅ Products loaded: ${orders.size}")
      orders
    } catch (e: Exception) {
      println("❌ Exception: ${e.message}")
      emptyList()
    }
  }
}
