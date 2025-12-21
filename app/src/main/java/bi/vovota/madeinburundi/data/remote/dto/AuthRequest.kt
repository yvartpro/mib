package bi.vovota.madeinburundi.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phone: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phone: String,
)