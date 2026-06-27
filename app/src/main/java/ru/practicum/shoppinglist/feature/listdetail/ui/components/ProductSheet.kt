package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.components.DropdownTextField
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun ProductSheet(add: () -> Unit) {
    var product by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    val expandedProduct = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(horizontal = Dimens.padding16)
    ) {
        DropdownTextField(
            value = product,
            onValueChange = { product = it },
            items = listOf("w", "w", "w2", "w", "w4", "w", "w6", "w", "w8", "w", "w"),
            labelId = R.string.listdetail_productsheet_product_label,
            placeholderId = R.string.listdetail_productsheet_product_placeholder,
            expanded = expandedProduct,
            modifier = Modifier.fillMaxWidth(),

        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.padding16)) {
            AppTextField(
                value = quantity,
                onValueChange = { quantity = it },
                labelId = R.string.listdetail_productsheet_quantity_label,
                placeholderId = R.string.listdetail_productsheet_quantity_label,
                modifier = Modifier.weight(1f),
            )
            DropdownTextField(
                value = unit,
                onValueChange = { unit = "ProductUnit.L" },
                labelId = R.string.listdetail_productsheet_unit_label,
                placeholderId = R.string.listdetail_productsheet_unit_label,
                exposed = true,
                items = listOf("w", "e", "r"),
                modifier = Modifier.weight(1f),
                readOnly = true
            )
        }
        Spacer(modifier = Modifier.height(112.dp)) // if (expandedProduct.value) 122.dp else 0.dp))
    }
}

@AppPreview
@Composable
private fun ProductSheetPreview() {
    AppTheme {
        ProductSheet {}
    }
}
