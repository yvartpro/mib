package bi.vovota.madeinburundi.data.model

import androidx.annotation.StringRes
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
  val category: String,
  val tax: String?="",
  val transport: String?="",
  @SerialName("is_box") val isBox: Boolean,
  @SerialName("add_at") val addedAt: String?,
  @SerialName("product_photo") val image: String,
  val company: Company,
  @SerialName("bdi_price") val bdiPrice: String,
  @SerialName("drc_price") val drcPrice: String,
  @SerialName("ke_price") val kePrice: String,
  @SerialName("ug_price") val ugPrice: String,
  @SerialName("rw_price") val rwPrice: String,
  @SerialName("tz_price") val tzPrice: String
)

@Serializable
data class CartItem(
  val id: Int,
  val product: Product,
  @Transient
  var quantity: MutableState<Int> = mutableIntStateOf(1)
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
  val photo: String? = null,
) {
  val code: String = phone.take(3)
}

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
data class NewOrder(
  val customer: Int,
  val description: String
)

data class Category(@StringRes val title: Int, val name: String? = null, var isActive: Boolean)

@Serializable
data class UserUpdate(
  val user: UserFields,
  val address: String? = null,
)

@Serializable
data class UserFields(
  @SerialName("full_name") val fullName: String? = null,
  @SerialName("phone_number") val phone: String? = null,
  val password: String? = null,
)

@Serializable
data class Order(
  val id: Int,
  val description: String,
  @SerialName("is_delivered") val isDelivered: Boolean,
  @SerialName("is_payed") val isPayed: Boolean,
  val customer: Int,
  val date: String
)