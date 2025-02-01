package com.example.advancedandroidassignment1

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// Initialize the DataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

