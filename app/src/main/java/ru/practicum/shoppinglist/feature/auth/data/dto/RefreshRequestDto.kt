package ru.practicum.shoppinglist.feature.auth.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)
