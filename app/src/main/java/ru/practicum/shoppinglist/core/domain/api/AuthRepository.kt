package ru.practicum.shoppinglist.core.domain.api

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun userId(): Flow<Long?>
    suspend fun register(login: String, password: String)
    suspend fun login(login: String, password: String)
    suspend fun recovery(email: String): String
    suspend fun check(): Boolean
    suspend fun logout()
}
