package ru.practicum.shoppinglist.feature.auth.domain.api

interface AuthRepository {
    suspend fun register(login: String, password: String)
    suspend fun login(login: String, password: String)
    suspend fun recovery(email: String): Boolean
    suspend fun check(): Boolean
}
