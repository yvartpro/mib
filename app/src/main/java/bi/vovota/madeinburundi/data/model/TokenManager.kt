package bi.vovota.madeinburundi.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


object TokenManager {
  private lateinit var dataStore: DataStore<Preferences>
  private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
  private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
  private val USER_ID = intPreferencesKey("user_id")
  private val USER_NAME = stringPreferencesKey("user_name")
  private val USER_PHONE = stringPreferencesKey("user_phone")

  fun init(context: Context) {
    dataStore = context.userPrefsDataStore
  }

  suspend fun saveTokens(access: String, refresh: String) {
    dataStore.edit {
      it[ACCESS_TOKEN_KEY] = access
      it[REFRESH_TOKEN_KEY] = refresh
    }
  }

  suspend fun saveUser(user: User) {
    println(user)
    dataStore.edit {
      it[USER_ID] = user.id
      it[USER_NAME] = user.fullName
      it[USER_PHONE] = user.phone
    }
  }
    suspend fun getUser(): User? {
        return dataStore.data
            .map { prefs ->
                val id = prefs[USER_ID]
                val name = prefs[USER_NAME]
                val phone = prefs[USER_PHONE]

                if (id != null && name != null && phone != null) {
                    User(
                        id = id,
                        fullName = name,
                        phone = phone
                    )
                } else {
                    null
                }
            }
            .firstOrNull()
    }

    suspend fun getAccessToken(): String? = dataStore.data.map {
    it[ACCESS_TOKEN_KEY]  }.firstOrNull().also { println("Access token got: $it") }

  suspend fun getRefreshToken(): String? = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.firstOrNull()
  suspend fun clearTokens() {
    dataStore.edit {
      it.remove(ACCESS_TOKEN_KEY)
      it.remove(REFRESH_TOKEN_KEY)
    }
  }
    suspend fun clearAll() {
        dataStore.edit {
            it.remove(ACCESS_TOKEN_KEY)
            it.remove(REFRESH_TOKEN_KEY)
            it.remove(USER_ID)
            it.remove(USER_NAME)
            it.remove(USER_PHONE)
        }
    }

}
