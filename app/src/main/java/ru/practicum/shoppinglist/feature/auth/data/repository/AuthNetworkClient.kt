package ru.practicum.shoppinglist.feature.auth.data.repository

import ru.practicum.shoppinglist.core.data.network.NetworkResponse
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.RefreshDto
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.UserDto

interface AuthNetworkClient {
    suspend fun register(email: String, password: String): NetworkResponse<UserDto?>
    suspend fun login(email: String, password: String): NetworkResponse<UserDto?>
    suspend fun refresh(token: String): NetworkResponse<RefreshDto?>
    suspend fun check(token: String): NetworkResponse<Boolean?>
    suspend fun recovery(email: String): NetworkResponse<Boolean?>
}
