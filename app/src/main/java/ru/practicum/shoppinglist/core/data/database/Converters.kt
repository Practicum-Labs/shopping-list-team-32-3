package ru.practicum.shoppinglist.core.data.database

import androidx.room.TypeConverter
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Unit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

class Converters {
    @TypeConverter
    fun fromUnitToString(unit: Unit?): String? {
        return unit?.toString()
    }

    @TypeConverter
    fun fromStringToUnit(value: String?): Unit? {
        return value?.let { Unit.valueOf(it) }
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
