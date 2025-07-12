package bi.vovota.madeinburundi.data.repository

import bi.vovota.madeinburundi.data.model.CartItem
import bi.vovota.madeinburundi.data.model.NewOrder
import bi.vovota.madeinburundi.data.model.Order
import bi.vovota.madeinburundi.data.model.Product
import bi.vovota.madeinburundi.data.model.TokenManager
import bi.vovota.madeinburundi.data.model.User
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
  suspend fun placeOrderMessage(user: User, cartItems: List<CartItem>): Boolean {

    val description = buildOrderDescription(user, cartItems)
    return placeOrder(NewOrder(user.id, description))
  }

  private fun buildOrderDescription(user: User, items: List<CartItem>): String {
    fun getCurrency(): String {
      return when (user.code) {
        "254" -> "KSH"
        "255" -> "TSH"
        "250" -> "RWF"
        "243" -> "FC"
        "256" -> "UGX"
        else -> "FBU"
      }
    }

    fun getPrice(user: User, product: Product): String {
      return when (user.code) {
        "254" -> product.kePrice
        "255" -> product.tzPrice
        "250" -> product.rwPrice
        "243" -> product.drcPrice
        "256" -> product.ugPrice
        else -> product.bdiPrice
      }
    }
    val total = items.sumOf { getPrice(user, it.product).toDouble() * it.quantity.value }
    return buildString {
      append("Command details: \n")
      items.forEach {
        val sub = getPrice(user, it.product).toDouble() * it.quantity.value
        append("- ${it.quantity.value}x ${it.product.name} (${getPrice(user, it.product)} ${getCurrency()}): ${sub.toInt()} ${getCurrency()}\n")
      }
      append("\nTotal: ${total.toInt()} ${getCurrency()}")
    }
  }
  suspend fun getOrders(): List<Order> {
    val access = TokenManager.getAccessToken()
    println("Passed access_token: $access")
    val token = access ?: println("No token found")
    return try {
      println("🔎 Calling order API...")
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
