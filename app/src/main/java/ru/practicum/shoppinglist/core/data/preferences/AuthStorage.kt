package ru.practicum.shoppinglist.core.data.preferences

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthStorage(private val dataStore: DataStore<AuthPreferences>) {

    fun userId(): Flow<Long?> {
        return dataStore.data.map { preferences ->
            if (preferences.hasUserId()) {
                preferences.userId
            } else {
                null
            }
        }
    }

    suspend fun accessToken(): String? {
        return dataStore.data.first().accessToken.ifEmpty { null }
    }

    suspend fun refreshToken(): String? {
        return dataStore.data.first().refreshToken.ifEmpty { null }
    }

    suspend fun save(user: Long, access: String, refresh: String) {
        dataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .setUserId(user)
                .setAccessToken(access)
                .setRefreshToken(refresh)
                .build()
        }
    }

    suspend fun update(access: String, refresh: String) {
        dataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .setAccessToken(access)
                .setRefreshToken(refresh)
                .build()
        }
    }

    suspend fun clearSession() {
        dataStore.updateData {
            it.toBuilder()
                .clearAccessToken()
                .clearRefreshToken()
                .clearUserId()
                .build()
        }
    }
}
