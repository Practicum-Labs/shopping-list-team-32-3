package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector

fun iconForKey(key: String): ImageVector = when (key) {
    "shopping_cart" -> Icons.Outlined.ShoppingCart
    "home" -> Icons.Outlined.Home
    "restaurant" -> Icons.Outlined.Restaurant
    "cake" -> Icons.Outlined.Cake
    "work" -> Icons.Outlined.Work
    "favorite" -> Icons.Outlined.Favorite
    "star" -> Icons.Outlined.Star
    else -> Icons.AutoMirrored.Outlined.ListAlt
}
