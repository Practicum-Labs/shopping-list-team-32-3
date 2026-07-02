package ru.practicum.shoppinglist.feature.auth.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.practicum.shoppinglist.core.data.preferences.AuthPreferences

class AuthStorage(private val dataStore: DataStore<AuthPreferences>) {

    suspend fun userId(): Long? {
        return if (dataStore.data.first().hasUserId()) dataStore.data.first().userId else null
    }

    suspend fun accessToken(): String? {
        return dataStore.data.first().accessToken
    }

    suspend fun refreshToken(): String? {
        return dataStore.data.first().refreshToken
    }

    suspend fun isLoggedIn(): Boolean {
        return accessToken() != null
    }

    suspend fun save(user: Long, access: String, refresh: String){
        dataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .setUserId(user)
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