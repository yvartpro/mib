package com.example.madeinburundi.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences

val Context.userPrefsDataStore : DataStore<Preferences> by preferencesDataStore(name = "user_prefs")