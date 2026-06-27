package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.text.style.TextOverflow
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.ColorsGreen
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.mock1
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.mock2
import ru.practicum.shoppinglist.feature.listdetail.ui.utils.UnitStringMapper

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mapper = UnitStringMapper(LocalContext.current)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding16, vertical = Dimens.padding14)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.icon24)
                .border(
                    border = BorderStroke(Dimens.border1, ColorsGreen)
                )
                .clickable(onClick = onCheck),
        ) {
            Icon(
                painter = painterResource(
                    if (product.isPurchased) R.drawable.ic_core_radio_checked else R.drawable.ic_core_radio_unchecked
                ),
                contentDescription = null,
                modifier = Modifier.size(Dimens.icon24),
                tint = Color.Unspecified
            )
        }

        Column {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (product.isPurchased) LineThrough else null
            )
            if (product.quantity != null && product.unit != null) {
                Text(
                    text = "${product.quantity} " + mapper.map(product.unit),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@AppPreview
@Composable
private fun ProductCardPreview() {
    AppTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ProductCard(
                Product.mock1(),
                {},
                {}
            )
            ProductCard(
                Product.mock2(),
                {},
                {}
            )
        }
    }
}
