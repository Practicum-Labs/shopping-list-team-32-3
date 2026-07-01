package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.components.DropdownTextField
import ru.practicum.shoppinglist.core.ui.components.Fab
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit
import ru.practicum.shoppinglist.feature.listdetail.ui.utils.UnitStringMapper

@Composable
fun ProductSheet(add: (String, Double, ProductUnit) -> Unit, onDismiss: () -> Unit) {
    val product = rememberTextFieldState("")
    var quantity = remember { mutableStateOf("") }
    val unit = rememberTextFieldState("")
    var allowAdd by remember { mutableStateOf(false) }
    val mapper = UnitStringMapper(LocalContext.current)

    LaunchedEffect(product.text, quantity.value, unit.text) {
        allowAdd = product.text.isNotEmpty() &&
            unit.text.isNotEmpty() &&
            quantity.value.isNotEmpty() &&
            quantity.value.toDouble() > 0
    }

    AppModalBottomSheet(
        fab = {
            if (allowAdd) {
                Fab(
                    R.drawable.ic_core_ok_fab_icon,
                    onClick = {
                        mapper.map(unit.text.toString())?.let {
                            add(product.text.toString(), quantity.value.toDouble(), it)
                            onDismiss()
                        }
                    }
                )
            }
        },
        onDismiss = onDismiss
    ) {
        ProductForm(product, quantity, unit, mapper.getList())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductForm(
    product: TextFieldState,
    quantity: MutableState<String>,
    unit: TextFieldState,
    units: List<String>
) {
    val expandedProduct = remember { mutableStateOf(false) }
    val expandedUnit = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = Dimens.padding16)
            .then(
                if (expandedProduct.value) {
                    Modifier.height(296.dp)
                } else if (expandedUnit.value) {
                    Modifier.height(322.dp)
                } else {
                    Modifier
                }
            )
    ) {
        DropdownTextField(
            state = product,
            items = emptyList(), // TODO T-41 — Detail: автоподсказки — UI подсказок (FR-LIST-07) #43
            labelId = R.string.listdetail_productsheet_product_label,
            placeholderId = R.string.listdetail_productsheet_product_placeholder,
            expanded = expandedProduct,
            modifier = Modifier.fillMaxWidth(),
            readOnly = false,
            keepIn = 160.dp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.padding16)) {
            AppTextField(
                value = quantity.value,
                onValueChange = { quantity.value = it },
                labelId = R.string.listdetail_productsheet_quantity_label,
                placeholderId = R.string.listdetail_productsheet_quantity_label,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            DropdownTextField(
                keepIn = 120.dp,
                state = unit,
                items = units,
                labelId = R.string.listdetail_productsheet_unit_label,
                placeholderId = R.string.listdetail_productsheet_unit_label,
                modifier = Modifier.weight(1f),
                readOnly = true,
                expanded = expandedUnit,
            )
        }
    }
}

@AppPreview
@Composable
private fun ProductFormPreview() {
    AppTheme {
        ProductSheet({ _, _, _ ->
        }) {}
    }
}
