package ru.practicum.shoppinglist.feature.lists.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.repository.ListsRepositoryImpl
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.ui.DetailViewModelFactory
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModel
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModelBase

val listsModule = module {
    single<ListDao> { get<AppDatabase>().listDao() }
    single<ListsRepository> { ListsRepositoryImpl(get(), get(), get()) }
    viewModel<ListsViewModelBase> {
        ListsViewModel(get(), get())
    }

    single<DetailViewModelFactory> { KoinDetailViewModelFactory() }
}

class KoinDetailViewModelFactory : KoinComponent, DetailViewModelFactory {
    override fun create(id: Long): ListDetailViewModelBase {
        return get<ListDetailViewModelBase> { parametersOf(id) }
    }
}
