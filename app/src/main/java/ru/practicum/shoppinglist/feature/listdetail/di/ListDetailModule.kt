package ru.practicum.shoppinglist.feature.listdetail.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.listdetail.data.dao.ProductDao
import ru.practicum.shoppinglist.feature.listdetail.data.repository.ProductsRepositoryImpl
import ru.practicum.shoppinglist.feature.listdetail.domain.api.ProductsRepository
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModel
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase

val listDetailModule = module {
    single<ProductDao> { get<AppDatabase>().productDao() }
    single<ProductsRepository> { ProductsRepositoryImpl(get()) }
    viewModel<ListDetailViewModelBase> {
        ListDetailViewModel(get(), get(), get())
    }
}
