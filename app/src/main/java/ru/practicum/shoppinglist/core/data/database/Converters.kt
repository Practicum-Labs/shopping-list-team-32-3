package ru.practicum.shoppinglist.core.data.database

import androidx.room.TypeConverter
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

class Converters {
    @TypeConverter
    fun fromUnitToString(unit: ProductUnit?): String? {
        return unit?.toString()
    }

    @TypeConverter
    fun fromStringToUnit(value: String?): ProductUnit? {
        return value?.let { ProductUnit.valueOf(it) }
    }

    @TypeConverter
    fun fromSortModeToString(sortMode: SortMode?): String? {
        return sortMode?.name
    }

    @TypeConverter
    fun fromStringToSortMode(value: String?): SortMode? {
        return value?.let { SortMode.valueOf(it) }
    }
}
