package com.example.madeinburundi.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object UserManager {
    private lateinit var dataStore: DataStore<Preferences>
    private val USER_KEY = stringSetPreferencesKey("active_user")

    fun init(context: Context) {
        dataStore = context.userPrefsDataStore
    }

    suspend fun saveUser(user: User) {
        val jsonUser = Json.encodeToString(user)
        println("User saved: $user")
        dataStore.edit { it[USER_KEY] = setOf(jsonUser) }
    }

    suspend fun getUser(): User? {
        return dataStore.data.map { it[USER_KEY] }.mapNotNull { it?.let { Json.decodeFromString<User>(
            it.toString()
        ) } }.firstOrNull()
    }
}