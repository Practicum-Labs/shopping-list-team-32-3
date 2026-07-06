package ru.practicum.shoppinglist.feature.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckDto(
    @SerialName("is_valid")
    val isValid: Boolean?,
    val success: Boolean?,
)
