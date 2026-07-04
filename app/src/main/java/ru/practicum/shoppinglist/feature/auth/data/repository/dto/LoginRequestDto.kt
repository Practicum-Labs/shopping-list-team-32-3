package ru.practicum.shoppinglist.feature.auth.data.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)
