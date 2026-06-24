package ru.practicum.shoppinglist.feature.listdetail.ui.utils

import android.content.Context
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Unit

class UnitStringMapper {
    fun map(unit: Unit, context: Context): String {
        return context.getString(
            when (unit) {
                Unit.L -> R.string.listdetail_unit_liter
                Unit.ML -> R.string.listdetail_unit_milliliter
                Unit.PACK -> R.string.listdetail_unit_pack
                Unit.PACKET -> R.string.listdetail_unit_packet
                Unit.PCS -> R.string.listdetail_unit_piece
                Unit.KG -> R.string.listdetail_unit_kilogram
                Unit.G -> R.string.listdetail_unit_gram
            }
        )
    }
}
