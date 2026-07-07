package ru.practicum.shoppinglist.core.data.repository

import ru.practicum.shoppinglist.core.domain.exception.DataException
import ru.practicum.shoppinglist.feature.auth.data.AuthNetworkClient
import ru.practicum.shoppinglist.feature.auth.data.AuthStorage
import ru.practicum.shoppinglist.feature.auth.data.dto.RefreshDto
import ru.practicum.shoppinglist.feature.auth.data.dto.UserDto
import ru.practicum.shoppinglist.feature.auth.domain.api.AuthRepository

class AuthRepositoryImpl(
    val client: AuthNetworkClient,
    val storage: AuthStorage
) : AuthRepository {
    override suspend fun register(login: String, password: String) {
        val response = client.register(login, password)
        if (response.data is UserDto) {
            save(response.data)
        } else {
            throw DataException.Network(response.error ?: "")
        }
    }
    override suspend fun login(login: String, password: String) {
        val response = client.login(login, password)
        if (response.data is UserDto) {
            save(response.data)
        } else {
            throw DataException.Network(response.error ?: "")
        }
    }
    override suspend fun recovery(email: String): String {
        val response = client.recovery(email)
        if (response.data is String) {
            return response.data
        } else {
            throw DataException.Network(response.error ?: "")
        }
    }

    override suspend fun check(): Boolean {
        storage.accessToken()?.let {
            val response = client.check("Bearer $it")
            if (response.data?.isValid == true || refresh()) {
                return true
            }
        }

        logout()
        return false
    }

    override suspend fun logout() {
        storage.clearSession()
    }

    private suspend fun refresh(): Boolean {
        storage.refreshToken()?.let {
            val response = client.refresh(it)
            if (response.data is RefreshDto) {
                storage.update(
                    response.data.accessToken,
                    response.data.refreshToken
                )
                return true
            }
        }

        return false
    }

    private suspend fun save(dto: UserDto) {
        storage.save(
            dto.userId,
            dto.accessToken,
            dto.refreshToken
        )
    }
}
