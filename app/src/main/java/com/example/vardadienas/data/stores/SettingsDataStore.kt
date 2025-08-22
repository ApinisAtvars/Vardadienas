package com.example.vardadienas.data.stores

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a singleton instance of DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val DAILY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("daily_notifications_enabled")
        // User can choose what time the daily notifications are sent
        val NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
        val NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
    }

    val dailyNotificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DAILY_NOTIFICATIONS_ENABLED] ?: false
    }

    val notificationTime: Flow<Pair<Int, Int>> = dataStore.data.map { preferences ->
        val hour = preferences[NOTIFICATION_HOUR] ?: 9
        val minute = preferences[NOTIFICATION_MINUTE] ?: 0
        Pair(hour, minute)
    }

    suspend fun setDailyNotificationsEnabled(isEnabled: Boolean) {
        dataStore.edit { settings ->
            settings[DAILY_NOTIFICATIONS_ENABLED] = isEnabled
        }
    }

    suspend fun saveNotificationTime(hour: Int, minute: Int) {
        dataStore.edit { settings ->
            settings[NOTIFICATION_HOUR] = hour
            settings[NOTIFICATION_MINUTE] = minute
        }
    }
}