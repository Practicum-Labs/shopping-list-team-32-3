package ru.practicum.shoppinglist.feature.auth.data.repository.network

import retrofit2.Response
import ru.practicum.shoppinglist.core.data.network.NetworkResponse
import ru.practicum.shoppinglist.feature.auth.data.repository.AuthNetworkClient
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.LoginRequestDto
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.RefreshDto
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.RefreshRequestDto
import ru.practicum.shoppinglist.feature.auth.data.repository.dto.UserDto

@Suppress("TooGenericExceptionCaught")
class RetrofitAuthApiNetworkClient(val api: AuthApi) : AuthNetworkClient {
    override suspend fun register(
        email: String,
        password: String
    ): NetworkResponse<UserDto?> = apiCall {
        api.register(LoginRequestDto(email, password))
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResponse<UserDto?> = apiCall {
        api.login(LoginRequestDto(email, password))
    }

    override suspend fun refresh(
        token: String
    ): NetworkResponse<RefreshDto?> = apiCall {
        api.refresh(RefreshRequestDto(refreshToken = token))
    }

    override suspend fun check(
        token: String
    ): NetworkResponse<Boolean?> = apiCall {
        api.check(token)
    }

    override suspend fun recovery(
        email: String
    ): NetworkResponse<Boolean?> = apiCall {
        api.recovery(email)
    }

    private suspend fun <T> apiCall(
        block: suspend () -> Response<T>
    ): NetworkResponse<T?> {
        return try {
            val response = block()
            NetworkResponse(response.body(), response.code().toString())
        } catch (e: Exception) {
            NetworkResponse(null, e.message ?: "-1")
        }
    }
}
