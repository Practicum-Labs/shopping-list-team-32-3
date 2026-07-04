package ru.practicum.shoppinglist.feature.auth.data.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)
