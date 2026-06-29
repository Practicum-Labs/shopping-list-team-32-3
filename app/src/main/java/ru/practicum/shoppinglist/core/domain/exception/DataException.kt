package ru.practicum.shoppinglist.core.domain.exception

sealed class DataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Database(message: String, cause: Throwable? = null) : DataException(message, cause)
    class Network(message: String, cause: Throwable? = null) : DataException(message, cause)
    class InvalidData(message: String, cause: Throwable? = null) : DataException(message, cause)
}
