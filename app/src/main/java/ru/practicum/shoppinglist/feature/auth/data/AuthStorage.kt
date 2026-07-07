package ru.practicum.shoppinglist.feature.auth.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import ru.practicum.shoppinglist.core.data.preferences.AuthPreferences

class AuthStorage(private val dataStore: DataStore<AuthPreferences>) {

    suspend fun userId(): Long? {
        return if (dataStore.data.first().hasUserId()) dataStore.data.first().userId else null
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
