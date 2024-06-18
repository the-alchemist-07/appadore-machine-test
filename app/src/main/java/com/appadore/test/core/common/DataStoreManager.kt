package com.appadore.test.core.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveScheduledTime(scheduledTime: Long) {
        dataStore.edit { preferences ->
            preferences[SCHEDULED_TIME] = scheduledTime
            preferences[GAME_STATUS] = Constants.GAME_STATUS_SCHEDULED
            preferences[GAME_SCORE] = 0
        }
    }

    companion object {
        val SCHEDULED_TIME = longPreferencesKey("SCHEDULED_TIME")
        val GAME_STATUS = stringPreferencesKey("GAME_STATUS")
        val GAME_SCORE = intPreferencesKey("GAME_SCORE")
    }
}
