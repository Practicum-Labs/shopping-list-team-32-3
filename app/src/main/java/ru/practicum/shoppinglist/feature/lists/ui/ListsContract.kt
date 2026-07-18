package ru.practicum.shoppinglist.feature.lists.ui

import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList

interface ListsContract {

    data class State(
        val lists: List<ShoppingList> = emptyList(),
        val isLoading: Boolean = true,
        val activeSheet: Sheet? = null,
        val query: String = "",
        val error: String? = null,
        val swipeResetToken: Int = 0,
    ) : UiState

    sealed interface Sheet {
        data object AddList : Sheet
        data class SelectIcon(val id: Long) : Sheet
        data class Rename(val id: Long, val currentName: String) : Sheet
        data class ConfirmDelete(val id: Long, val name: String) : Sheet
        data object ConfirmDeleteAll : Sheet
    }

    sealed interface Intent : UiIntent {
        data object Load : Intent
        data class OpenList(val id: Long) : Intent
        data object OpenAddSheet : Intent
        data object DismissSheet : Intent
        data class CreateList(val name: String) : Intent
        data object OpenLogoutConfirm : Intent
        data object Logout : Intent
        data class RenameList(val id: Long) : Intent
        data class ConfirmRename(val id: Long, val name: String) : Intent
        data class DuplicateList(val id: Long) : Intent
        data class RequestDelete(val id: Long) : Intent
        data class ConfirmDelete(val id: Long) : Intent
        data object RequestDeleteAll : Intent
        data object DeleteAllLists : Intent
        data class OpenIconsSheet(val id: Long) : Intent
        data class ChangeIcon(val icon: String, val id: Long) : Intent
    }

    sealed interface Effect : UiEffect {
        data class OpenList(val id: Long) : Effect
        data object ShowLogoutDialog : Effect
        data object NavigateToLogin : Effect
    }
}
