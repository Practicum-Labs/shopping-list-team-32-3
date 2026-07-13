package ru.practicum.shoppinglist.feature.auth.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.practicum.shoppinglist.feature.auth.data.dto.CheckDto
import ru.practicum.shoppinglist.feature.auth.data.dto.LoginRequestDto
import ru.practicum.shoppinglist.feature.auth.data.dto.RefreshDto
import ru.practicum.shoppinglist.feature.auth.data.dto.RefreshRequestDto
import ru.practicum.shoppinglist.feature.auth.data.dto.UserDto

interface AuthApi {
    @POST("/auth/registration")
    suspend fun register(@Body request: LoginRequestDto): Response<UserDto>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<UserDto>

    @POST("/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): Response<RefreshDto>

    @GET("/auth/check")
    suspend fun check(@Header("Authorization") bearer: String): Response<CheckDto>

    @POST("/auth/recovery")
    suspend fun recovery(@Header("email") email: String): Response<Unit>
}
