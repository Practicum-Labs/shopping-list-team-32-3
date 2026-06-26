package ru.practicum.shoppinglist.feature.lists.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository

class ListsViewModel(
    private val repository: ListsRepository,
) : MviViewModel<ListsContract.State, ListsContract.Intent, ListsContract.Effect>(
    initial = ListsContract.State(),
) {

    private var observeJob: Job? = null

    init {
        observeLists()
    }

    override fun onIntent(intent: ListsContract.Intent) {
        when (intent) {
            is ListsContract.Intent.Load -> observeLists()
            is ListsContract.Intent.OpenList -> openList(intent.id)
            is ListsContract.Intent.OpenAddSheet ->
                setState { copy(activeSheet = ListsContract.Sheet.AddList) }
            is ListsContract.Intent.DismissSheet ->
                setState { copy(activeSheet = null) }
            is ListsContract.Intent.CreateList -> createList(intent.name)
        }
    }

    private fun createList(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            repository.create(trimmed)
            setState { copy(activeSheet = null) }
        }
    }

    private fun observeLists() {
        observeJob?.cancel()
        observeJob = repository.observeLists()
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
