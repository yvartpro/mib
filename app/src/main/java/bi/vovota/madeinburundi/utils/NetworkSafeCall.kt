package bi.vovota.madeinburundi.utils

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.*

suspend fun <T> safeApiCall(
  apiCall: suspend () -> T
): Result<T> {
  return try {
    Result.success(apiCall())
  } catch (throwable: Throwable) {
    val message = when (throwable) {
      is ClientRequestException -> {
        val errorBody = throwable.response.bodyAsText()
        extractErrorMessage(errorBody)
      }
      is ServerResponseException -> "Erreur serveur (${throwable.response.status.value})."
      is RedirectResponseException -> "Erreur de redirection inattendue."
      is IOException -> "Erreur réseau. Vérifiez votre connexion Internet."
      else -> throwable.message ?: "Erreur inconnue."
    }
    Result.failure(kotlin.Exception(message))
  }
}

fun extractErrorMessage(json: String): String {
  return try {
    Regex("\"detail\"\\s*:\\s*\"([^\"]+)\"").find(json)?.groupValues?.get(1)
      ?: Regex("\"[a-zA-Z_]+\"\\s*:\\s*\\[\\s*\"([^\"]+)\"\\s*]").find(json)?.groupValues?.get(1)
      ?: "Une erreur est survenue."
  } catch (e: Exception) {
    "Erreur inconnue."
  }
}