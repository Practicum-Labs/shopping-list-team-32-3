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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit
import ru.practicum.shoppinglist.feature.listdetail.ui.utils.UnitStringMapper

@Composable
fun ProductSheet(
    initialProduct: Product? = null,
    onSave: (String, Double?, ProductUnit?) -> Unit,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val product = rememberTextFieldState(initialProduct?.name ?: "")

    val initialQuantity = initialProduct?.quantity?.let {
        if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
    } ?: ""
    val quantity = remember { mutableStateOf(initialQuantity) }

    val unit = rememberTextFieldState("")
    var allowAdd by remember { mutableStateOf(product.text.isNotEmpty()) }
    val mapper = UnitStringMapper(LocalContext.current)

    LaunchedEffect(product.text, quantity.value, unit.text) {
        allowAdd = product.text.isNotEmpty()
    }

    AppModalBottomSheet(
        fab = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (initialProduct != null && onDelete != null) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.padding(end = Dimens.padding16)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                if (allowAdd) {
                    Fab(
                        R.drawable.ic_core_ok_fab_icon,
                        onClick = {
                            onSave(
                                product.text.toString(),
                                quantity.value.toDoubleOrNull(),
                                mapper.map(unit.text.toString())
                            )
                            onDismiss()
                        }
                    )
                }
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
    units: List<String>,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RoundButton(
                    R.drawable.ic_listdetail_minus_icon,
                    {
                        quantity.value = getQuantity(quantity.value, -1)
                    },
                    enabled = quantity.value.isNotEmpty()
                )
                RoundButton(
                    R.drawable.ic_listdetail_plus_icon,
                    {
                        quantity.value = getQuantity(quantity.value, 1)
                    }
                )
            }
        }
    }
}

fun getQuantity(quantity: String, add: Int): String {
    val quantityDouble = (quantity.toDoubleOrNull() ?: 0.0) + add
    if (quantityDouble > 0) {
        return quantityDouble.toString()
    }
    return "0.0"
}

@AppPreview
@Composable
private fun ProductFormPreview() {
    AppTheme {
        ProductSheet(
            initialProduct = null,
            onSave = { _, _, _ -> },
            onDismiss = {}
        )
    }
}