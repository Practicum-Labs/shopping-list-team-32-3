package ru.practicum.shoppinglist.feature.auth.data.repository.dto

data class LoginRequestDto(
    val email: String,
    val password: String,
)
