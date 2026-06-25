package ru.practicum.shoppinglist.feature.listdetail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.feature.listdetail.domain.api.ProductsRepository
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Unit
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

class ListDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val listRepository: ListsRepository,
    private val productsRepository: ProductsRepository
) : MviViewModel<ListDetailContract.State, ListDetailContract.Intent, ListDetailContract.Effect>(
    initial = ListDetailContract.State()
) {
    private var observeJob: Job? = null

    init {
        val listId = savedStateHandle.get<Long>("listId") ?: -1L
        if (listId != -1L) {
            onIntent(ListDetailContract.Intent.Load(listId))
        }
    }

    override fun onIntent(intent: ListDetailContract.Intent) {
        when (intent) {
            is ListDetailContract.Intent.Load -> loadList(intent.listId)
            is ListDetailContract.Intent.AddProduct -> addProduct(
                intent.name,
                intent.quantity,
                intent.unit
            )
            is ListDetailContract.Intent.EditProduct -> editProduct(intent.product)
            is ListDetailContract.Intent.DeleteProduct -> deleteProduct(intent.productId)
            is ListDetailContract.Intent.TogglePurchased -> togglePurchased(
                intent.productId,
                intent.isPurchased
            )
            is ListDetailContract.Intent.ClearPurchased -> clearPurchased(intent.listId)
            is ListDetailContract.Intent.SetSortMode -> setSortMode(intent.sortMode)
            is ListDetailContract.Intent.ReorderProducts -> reorderProducts(
                intent.fromPosition,
                intent.toPosition
            )
        }
    }

    private fun loadList(listId: Long) {
        setState {
            copy(
                listId = listId,
                isLoading = true
            )
        }

        viewModelScope.launch {
            val shoppingList = listRepository.getListById(listId)

            val listName = shoppingList?.name ?: "Список"
            val sortMode = shoppingList?.sortMode ?: SortMode.MANUAL

            observeJob?.cancel()
            observeJob = productsRepository.observeProducts(listId, sortMode)
                .onEach { products ->
                    setState {
                        copy(
                            listId = listId,
                            listName = listName,
                            products = products,
                            sortMode = sortMode,
                            isLoading = false
                        )
                    }
                }
                .catch {
                    setState { copy(isLoading = false) }
                    sendEffect(ListDetailContract.Effect.ShowToast("Ошибка загрузки: ${it.message}"))
                }
                .launchIn(viewModelScope)
        }
    }

    private fun addProduct(name: String, quantity: Double?, unit: Unit?) {
        viewModelScope.launch {
            try {
                val listId = currentState().listId
                productsRepository.addProduct(listId, name, quantity, unit)
                sendEffect(ListDetailContract.Effect.ShowToast("Товар добавлен"))
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun editProduct(product: Product) {
        viewModelScope.launch {
            try {
                productsRepository.updateProduct(product)
                sendEffect(ListDetailContract.Effect.ShowToast("Товар обновлён"))
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            try {
                productsRepository.deleteProduct(productId)
                sendEffect(ListDetailContract.Effect.ShowToast("Товар удалён"))
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun togglePurchased(productId: Long, isPurchased: Boolean) {
        viewModelScope.launch {
            try {
                productsRepository.setPurchased(productId, isPurchased)
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun clearPurchased(listId: Long) {
        viewModelScope.launch {
            try {
                productsRepository.clearPurchased(listId)
                sendEffect(ListDetailContract.Effect.ShowToast("Покупки очищены"))
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun setSortMode(sortMode: SortMode) {
        viewModelScope.launch {
            try {
                val listId = currentState().listId
                // Сохраняем режим сортировки
                listRepository.setSortMode(listId, sortMode)

                // Обновляем состояние
                setState {
                    copy(sortMode = sortMode)
                }

                // Переподписываемся с новой сортировкой
                loadList(listId)

                sendEffect(ListDetailContract.Effect.ShowToast("Сортировка: ${sortMode.name}"))
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

    private fun reorderProducts(fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            try {
                val listId = currentState().listId
                productsRepository.reorderProducts(listId, fromPosition, toPosition)
            } catch (e: Exception) {
                sendEffect(ListDetailContract.Effect.ShowToast("Ошибка: ${e.message}"))
            }
        }
    }

}