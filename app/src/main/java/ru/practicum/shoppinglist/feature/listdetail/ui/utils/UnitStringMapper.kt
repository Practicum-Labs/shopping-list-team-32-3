package ru.practicum.shoppinglist.feature.listdetail.ui.utils

import android.content.Context
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit

class UnitStringMapper(val context: Context) {
    fun map(unit: ProductUnit): String {
        return context.getString(
            when (unit) {
                ProductUnit.L -> R.string.listdetail_unit_liter
                ProductUnit.ML -> R.string.listdetail_unit_milliliter
                ProductUnit.PACK -> R.string.listdetail_unit_pack
                ProductUnit.PACKET -> R.string.listdetail_unit_packet
                ProductUnit.PCS -> R.string.listdetail_unit_piece
                ProductUnit.KG -> R.string.listdetail_unit_kilogram
                ProductUnit.G -> R.string.listdetail_unit_gram
            }
        )
    }

    fun map(unit: String): ProductUnit? {
        return when (unit) {
            context.getString(R.string.listdetail_unit_liter) -> ProductUnit.L
            context.getString(R.string.listdetail_unit_milliliter) -> ProductUnit.ML
            context.getString(R.string.listdetail_unit_pack) -> ProductUnit.PACK
            context.getString(R.string.listdetail_unit_packet) -> ProductUnit.PACKET
            context.getString(R.string.listdetail_unit_piece) -> ProductUnit.PCS
            context.getString(R.string.listdetail_unit_kilogram) -> ProductUnit.KG
            context.getString(R.string.listdetail_unit_gram) -> ProductUnit.G
            else -> null
        }
    }

    fun getList(): List<String> {
        return listOf(
            ProductUnit.L,
            ProductUnit.ML,
            ProductUnit.PACK,
            ProductUnit.PACKET,
            ProductUnit.PCS,
            ProductUnit.KG,
            ProductUnit.G
        )
            .map { map(it) }
    }
}
