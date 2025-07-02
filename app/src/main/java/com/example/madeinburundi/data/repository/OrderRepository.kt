package com.example.madeinburundi.data.repository

import com.example.madeinburundi.data.model.CartItem
import com.example.madeinburundi.data.model.Order
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject

class OrderRepository @Inject constructor(
  private val client: HttpClient
) {
  init {
    println("Received: ${client.hashCode()}")
  }
  suspend fun placeOrder(order: Order): Boolean {
    println("Sending...: ${order}")
    return try {
      val response = client.post("https://mib.vovota.bi/api/order/") {
        contentType(ContentType.Application.Json)
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
    return placeOrder(Order(customerId, description))
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
}
