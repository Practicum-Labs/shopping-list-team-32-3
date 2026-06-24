package ru.practicum.shoppinglist.feature.lists.ui

import androidx.lifecycle.viewModelScope
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

    init {
        observeLists()
    }

    override fun onIntent(intent: ListsContract.Intent) {
        when (intent) {
            is ListsContract.Intent.Load -> observeLists()
            is ListsContract.Intent.OpenList -> openList(intent.id)
        }
    }

    private fun observeLists() {
        repository.observeLists()
            .onEach { lists ->
                setState { copy(lists = lists, isLoading = false) }
            }
            .catch {
                setState { copy(isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun openList(id: Long) {
        viewModelScope.launch {
            sendEffect(ListsContract.Effect.OpenList(id))
        }
    }
}
