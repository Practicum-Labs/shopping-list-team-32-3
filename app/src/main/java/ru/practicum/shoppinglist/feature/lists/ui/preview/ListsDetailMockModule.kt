package ru.practicum.shoppinglist.feature.lists.ui.preview

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.ListDetailPreviewProvider
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.ListDetailViewModelMock
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.repository.ListsRepositoryImpl
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModel

val listsMockModule = module {
    viewModel<ListsViewModel>{
        ListsViewModel(get())
    }

    viewModel<ListDetailViewModelBase>{
        ListDetailPreviewProvider().values.first()
    }
}
