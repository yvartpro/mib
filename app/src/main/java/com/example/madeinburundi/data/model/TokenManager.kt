package com.example.madeinburundi.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


object TokenManager {
  private lateinit var dataStore: DataStore<Preferences>
  private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
  private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

  fun init(context: Context) {
    dataStore = PreferenceDataStoreFactory.create(produceFile = { context.preferencesDataStoreFile("user_prefs")})
  }

  suspend fun saveTokens(access: String, refresh: String) {
    dataStore.edit {
      it[ACCESS_TOKEN_KEY] = access
      it[REFRESH_TOKEN_KEY] = refresh
    }
  }

  suspend fun getAccessToken(): String? = dataStore.data.map { it[ACCESS_TOKEN_KEY] }.firstOrNull()
  suspend fun getRefreshToken(): String? = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.firstOrNull()
  suspend fun clearTokens() {
    dataStore.edit {
      it.remove(ACCESS_TOKEN_KEY)
      it.remove(REFRESH_TOKEN_KEY)
    }
  }
}