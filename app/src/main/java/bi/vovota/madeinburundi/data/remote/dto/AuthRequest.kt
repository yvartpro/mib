package bi.vovota.madeinburundi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phone: String,
    val password: String
)