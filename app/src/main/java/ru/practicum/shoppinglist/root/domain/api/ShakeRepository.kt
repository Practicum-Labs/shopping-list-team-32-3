package ru.practicum.shoppinglist.root.domain.api

import kotlinx.coroutines.flow.Flow

data object ShakeEvent

interface ShakeRepository {
    fun shakeEvents(): Flow<ShakeEvent>
}
