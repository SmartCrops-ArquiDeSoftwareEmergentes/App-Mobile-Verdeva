package com.example.nutricontrol

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Esta es la ÚNICA instancia global de DataStore
val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")