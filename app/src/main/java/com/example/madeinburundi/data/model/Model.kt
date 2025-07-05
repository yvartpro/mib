package com.example.madeinburundi.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Company(
  val id: Int,
  @SerialName("company_name") val name: String,
  val slogan: String? = null,
  @SerialName("established_year") val year: Int? = null,
  val description: String? = null,
  val logo: String
)

@Serializable
data class Product(
  val id: Int,
  @SerialName("product_name") val name: String,
  val price: String,
  val category: String,
  val tax: String?="",
  val transport: String?="",
  @SerialName("product_photo") val image: String,
  val company: Company
)

@Serializable
data class CartItem(
  val id: Int,
  val product: Product,
  @Transient
  var quantity: MutableState<Int> = mutableIntStateOf(1)
)

@Serializable
data class UserWrapper(
  val id:  Int,
  val user: User,
  val address: String? = null,
  val photo: String? = null
)
@Serializable
data class UserRaw(
  val id: Int,
  val user: NestedUser,
  val address: String? = null,
  val photo: String? = null
)
@Serializable
data class NestedUser(
  @SerialName("full_name") val fullName: String,
  @SerialName("phone_number") val phone: String,
)
fun UserRaw.toUser(): User = User(id, user.fullName, user.phone, address, photo)
@Serializable
data class User(
  val id: Int,
  val fullName: String,
  val phone: String,
  val address: String? = null,
  val photo: String? = null
)

@Serializable
data class UserRegister(
  @SerialName("full_name") val fullName: String,
  @SerialName("phone_number") val phone: String,
  val password: String
)

@Serializable
data class UserLogin(
  @SerialName("phone_number") val phone: String,
  val password: String
)

@Serializable
data class TokenResponse(
  val access: String,
  val refresh: String
)

@Serializable
data class Order(
  val customer: Int,
  val description: String
)

data class Category(val title: String, val name: String? = null, var isActive: Boolean)

@Serializable
data class UserUpdate(
  @SerialName("full_name") val name: String?,
  val address: String?
)