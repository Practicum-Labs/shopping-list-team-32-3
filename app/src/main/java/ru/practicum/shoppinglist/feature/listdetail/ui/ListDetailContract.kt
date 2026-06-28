package ru.practicum.shoppinglist.feature.listdetail.ui

import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

interface ListDetailContract {
    data class State(
        val listId: Long = -1L,
        val listName: String = "",
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val activeSheet: Sheet? = null,
        val sortMode: SortMode = SortMode.MANUAL
    ) : UiState

    sealed interface Sheet{
        data object Menu : Sheet
        data object SortSelection : Sheet
    }

    sealed interface Intent : UiIntent {
        data class Load(val listId: Long) : Intent
        data class AddProduct(val name: String, val quantity: Double?, val unit: ProductUnit?) : Intent
        data class EditProduct(val product: Product) : Intent
        data class DeleteProduct(val productId: Long) : Intent
        data class TogglePurchased(val productId: Long, val isPurchased: Boolean) : Intent
        data class ClearPurchased(val listId: Long) : Intent
        data class SetSortMode(val sortMode: SortMode) : Intent
        data class ReorderProducts(val fromPosition: Int, val toPosition: Int) : Intent
        data object OpenMenu : Intent
        data object CloseSheet : Intent
        data object OpenSort : Intent
        data object DeleteAllItems : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowToast(val message: String) : Effect
        data class ShowToastRes(val stringId: Int, val message: String = "") : Effect
        object NavigateBack : Effect
        data class NavigateToProductDetail(val productId: Long) : Effect
    }
}
