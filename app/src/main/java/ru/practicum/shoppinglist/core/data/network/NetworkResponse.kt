package ru.practicum.shoppinglist.core.data.network

data class NetworkResponse<T>(val data: T, val code: String, val error: String? = null)
