package com.example.advancedandroidassignment1

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Ensure this is declared **ONLY ONCE** in your project
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {
    private val idKey = stringPreferencesKey("id")
    private val usernameKey = stringPreferencesKey("username")
    private val courseNameKey = stringPreferencesKey("courseName")

    // Save Data
    suspend fun saveData(id: String, username: String, courseName: String) {
        context.dataStore.edit { prefs ->
            prefs[idKey] = id
            prefs[usernameKey] = username
            prefs[courseNameKey] = courseName
        }
    }

    // Load Data
    val loadData: Flow<Triple<String, String, String>> = context.dataStore.data.map { prefs ->
        Triple(
            prefs[idKey] ?: "",
            prefs[usernameKey] ?: "",
            prefs[courseNameKey] ?: ""
        )
    }

    // Clear Data
    suspend fun clearData() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}
