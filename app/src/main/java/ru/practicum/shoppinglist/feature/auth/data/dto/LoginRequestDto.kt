package ru.practicum.shoppinglist.feature.auth.data.dto

data class LoginRequestDto(
    val email: String,
    val password: String,
)
