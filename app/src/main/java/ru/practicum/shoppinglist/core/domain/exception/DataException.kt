package ru.practicum.shoppinglist.core.domain.exception

sealed class DataException : Exception() {
    data class Database(val error: String) : DataException()
    data class InvalidData(val error: String) : DataException()
    data class Network(val error: String) : DataException()
    data class Unknown(val error: String) : DataException()
}
