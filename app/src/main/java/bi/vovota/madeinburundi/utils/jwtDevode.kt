package bi.vovota.madeinburundi.utils

import android.util.Base64
import org.json.JSONObject
sealed class JwtResult {
    data class Success(val payload: JSONObject) : JwtResult()
    data class Error(val message: String) : JwtResult()
}

fun decodeJwtToken(token: String): JwtResult {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) {
            return JwtResult.Error("Invalid JWT structure")
        }

        val decoded = Base64.decode(
            parts[1],
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )

        JwtResult.Success(JSONObject(String(decoded)))
    } catch (e: Exception) {
        JwtResult.Error(e.message ?: "JWT decode failed")
    }
}
