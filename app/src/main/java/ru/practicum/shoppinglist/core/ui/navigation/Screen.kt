package ru.practicum.shoppinglist.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable sealed interface Screen {
    @Serializable data object Onboarding

    @Serializable data object Lists

    @Serializable data class ListDetail(val listId: Long)

    @Serializable data object Login

    @Serializable data object Registration

    @Serializable data object PasswordRecovery
}
