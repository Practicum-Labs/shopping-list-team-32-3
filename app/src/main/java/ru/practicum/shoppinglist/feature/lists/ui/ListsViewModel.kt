package ru.practicum.shoppinglist.feature.lists.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository

abstract class ListsViewModelBase(
    initial: ListsContract.State
) : MviViewModel<
    ListsContract.State,
    ListsContract.Intent,
    ListsContract.Effect
    > (initial)
class ListsViewModel(
    private val repository: ListsRepository,
    private val authRepository: AuthRepository,
) : ListsViewModelBase(
    initial = ListsContract.State(),
) {

    private var observeJob: Job? = null
    private var userId: Long? = null

    init {
        observeUser()
    }

    @Suppress("CyclomaticComplexMethod")
    override fun onIntent(intent: ListsContract.Intent) {
        when (intent) {
            is ListsContract.Intent.Load -> userId?.let { observeLists(it) }
            is ListsContract.Intent.OpenList -> openList(intent.id)
            is ListsContract.Intent.OpenAddSheet ->
                setState { copy(activeSheet = ListsContract.Sheet.AddList) }
            is ListsContract.Intent.DismissSheet ->
                setState { copy(activeSheet = null) }
            is ListsContract.Intent.CreateList -> userId?.let { createList(intent.name, it) }
            is ListsContract.Intent.OpenLogoutConfirm -> openConfirm()
            is ListsContract.Intent.Logout -> logout()
            is ListsContract.Intent.RenameList -> openRename(intent.id)
            is ListsContract.Intent.ConfirmRename -> renameList(intent.id, intent.name)
            is ListsContract.Intent.DuplicateList -> duplicateList(intent.id)
            is ListsContract.Intent.OpenIconsSheet ->
                setState { copy(activeSheet = ListsContract.Sheet.SelectIcon(intent.id)) }
            is ListsContract.Intent.ChangeIcon -> changeIcon(intent.icon, intent.id)
            is ListsContract.Intent.RequestDelete -> openDeleteConfirm(intent.id)
            is ListsContract.Intent.ConfirmDelete -> deleteList(intent.id)
            is ListsContract.Intent.RequestDeleteAll ->
                setState { copy(activeSheet = ListsContract.Sheet.ConfirmDeleteAll) }
            is ListsContract.Intent.DeleteAllLists -> userId?.let { deleteAllLists(it) }
        }
    }

    private fun createList(name: String, userId: Long) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            repository.create(trimmed, userId)
            setState { copy(activeSheet = null) }
        }
    }

    private fun openRename(id: Long) {
        val name = currentState().lists.find { it.id == id }?.name ?: return
        setState { copy(activeSheet = ListsContract.Sheet.Rename(id, name)) }
    }

    private fun renameList(id: Long, name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            repository.rename(id, trimmed)
            setState { copy(activeSheet = null, swipeResetToken = swipeResetToken + 1) }
        }
    }

    private fun openDeleteConfirm(id: Long) {
        val name = currentState().lists.find { it.id == id }?.name ?: return
        setState { copy(activeSheet = ListsContract.Sheet.ConfirmDelete(id, name)) }
    }

    private fun deleteList(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
            setState { copy(activeSheet = null, swipeResetToken = swipeResetToken + 1) }
        }
    }

    private fun duplicateList(id: Long) {
        viewModelScope.launch {
            repository.duplicate(id)
            setState { copy(swipeResetToken = swipeResetToken + 1) }
        }
    }

    private fun deleteAllLists(userId: Long) {
        viewModelScope.launch {
            repository.deleteAll(userId)
            setState { copy(activeSheet = null) }
        }
    }

    private fun openConfirm() {
        viewModelScope.launch {
            sendEffect(ListsContract.Effect.ShowLogoutDialog)
        }
    }
    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    private fun observeUser() {
        authRepository.userId().onEach { id ->
            userId = id

            if (id != null) {
                setState { copy(lists = emptyList(), isLoading = true, error = null) }
                observeLists(userId!!)
            } else {
                setState { copy(isLoading = false) }
                viewModelScope.launch {
                    sendEffect(ListsContract.Effect.NavigateToLogin)
                }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun observeLists(userId: Long) {
        observeJob?.cancel()
        observeJob = repository.observeLists(userId)
            .onEach { lists ->
                setState { copy(lists = lists, isLoading = false, error = null) }
            }
            .catch {
                setState { copy(isLoading = false, error = it.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun openList(id: Long) {
        viewModelScope.launch {
            sendEffect(ListsContract.Effect.OpenList(id))
        }
    }

    private fun changeIcon(key: String, id: Long) {
        viewModelScope.launch {
            repository.changeIcon(id, key)
            setState { copy(activeSheet = null) }
        }
    }
}
