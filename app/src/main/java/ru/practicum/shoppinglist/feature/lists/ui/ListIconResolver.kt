package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ImportContacts
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Snowboarding
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Stroller
import androidx.compose.material.icons.outlined.TempleBuddhist
import androidx.compose.material.icons.outlined.Toys
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

fun iconForKey(key: String): ImageVector {
    return iconForKey(getAppIcon(key))
}

private fun getAppIcon(key: String): AppIconKey? {
    return AppIconKey.entries.find { it.value == key }
}

@Suppress("CyclomaticComplexMethod")
fun iconForKey(key: AppIconKey?): ImageVector = when (key) {
    AppIconKey.SHOPPING_CART -> Icons.Outlined.ShoppingCart
    AppIconKey.SHOPPING_BAG -> Icons.Outlined.ShoppingBag
    AppIconKey.LOCAL_FLORIST -> Icons.Outlined.LocalFlorist
    AppIconKey.CONSTRUCTION -> Icons.Outlined.Construction
    AppIconKey.CELEBRATION -> Icons.Outlined.Celebration
    AppIconKey.CAKE -> Icons.Outlined.Cake
    AppIconKey.REDEEM -> Icons.Outlined.Redeem
    AppIconKey.LIQUOR -> Icons.Outlined.Liquor
    AppIconKey.PETS -> Icons.Outlined.Pets
    AppIconKey.TEMPLE -> Icons.Outlined.TempleBuddhist
    AppIconKey.FITNESS_CENTER -> Icons.Outlined.FitnessCenter
    AppIconKey.MEDICATION -> Icons.Outlined.Medication
    AppIconKey.SCHOOL -> Icons.Outlined.School
    AppIconKey.SNOWBOARDING -> Icons.Outlined.Snowboarding
    AppIconKey.HOME -> Icons.Outlined.Home
    AppIconKey.CHILD_CARE -> Icons.Outlined.ChildCare
    AppIconKey.ESPORTS -> Icons.Outlined.SportsEsports
    AppIconKey.PALETTE -> Icons.Outlined.Palette
    AppIconKey.APPAREL -> Icons.Outlined.Checkroom
    AppIconKey.DIRECTIONS_CAR -> Icons.Outlined.DirectionsCar
    AppIconKey.LUGGAGE -> Icons.Outlined.Luggage
    AppIconKey.CHAIR -> Icons.Outlined.Chair
    AppIconKey.SPA -> Icons.Outlined.Spa
    AppIconKey.PHOTO_CAMERA -> Icons.Outlined.PhotoCamera
    AppIconKey.IMPORT_CONTACTS -> Icons.Outlined.ImportContacts
    AppIconKey.SELF_IMPROVEMENT -> Icons.Outlined.SelfImprovement
    AppIconKey.TOYS -> Icons.Outlined.Toys
    AppIconKey.STROLLER -> Icons.Outlined.Stroller
    AppIconKey.DEVICES -> Icons.Outlined.Devices
    else -> Icons.AutoMirrored.Outlined.ListAlt
}

@Preview
@Composable
private fun IconsPreview() {
    AppTheme {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 68.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(AppIconKey.entries) { index, key ->
                Icon(
                    imageVector = iconForKey(key),
                    contentDescription = stringResource(R.string.lists_card_icon_description),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(Dimens.icon24),
                )
            }
        }
    }
}
