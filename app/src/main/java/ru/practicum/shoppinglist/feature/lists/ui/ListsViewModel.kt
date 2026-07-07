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
}
