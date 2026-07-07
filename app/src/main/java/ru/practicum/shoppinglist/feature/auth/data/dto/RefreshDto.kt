package ru.practicum.shoppinglist.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshDto(
    val accessToken: String,
    val refreshToken: String
)
