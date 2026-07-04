package ru.practicum.shoppinglist.feature.auth.data.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String
)
