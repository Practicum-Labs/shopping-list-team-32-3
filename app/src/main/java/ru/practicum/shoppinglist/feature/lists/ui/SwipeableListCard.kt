package ru.practicum.shoppinglist.feature.lists.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode
import kotlin.math.roundToInt

private enum class SwipeAnchor { Closed, Open, Dismiss }

@Composable
fun SwipeableListCard(
    list: ShoppingList,
    onClick: () -> Unit,
    onIconClick: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    resetSignal: Any? = null,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val currentOnDelete by rememberUpdatedState(onDelete)

    val actionsWidthPx = with(density) {
        (Dimens.icon48 * 3 + Dimens.padding8 * 3 + Dimens.padding16).toPx()
    }

    val state = remember { AnchoredDraggableState(initialValue = SwipeAnchor.Closed) }

    LaunchedEffect(state) {
        snapshotFlow { state.settledValue }.collect { settled ->
            if (settled == SwipeAnchor.Dismiss) {
                currentOnDelete()
                state.snapTo(SwipeAnchor.Closed)
            }
        }
    }

    LaunchedEffect(resetSignal) {
        if (state.currentValue != SwipeAnchor.Closed) {
            state.animateTo(SwipeAnchor.Closed)
        }
    }

    BoxWithConstraints(modifier) {
        val fullWidthPx = with(density) { maxWidth.toPx() }
        val anchors = remember(fullWidthPx, actionsWidthPx) {
            DraggableAnchors {
                SwipeAnchor.Closed at 0f
                SwipeAnchor.Open at -actionsWidthPx
                SwipeAnchor.Dismiss at -fullWidthPx
            }
        }
        SideEffect { state.updateAnchors(anchors) }

        val fullSwipe = state.targetValue == SwipeAnchor.Dismiss

        SwipeActionsBackground(
            fullSwipe = fullSwipe,
            onRename = onRename,
            onDuplicate = onDuplicate,
            onDelete = onDelete,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(Dimens.radius12)),
        )

        ListCard(
            list = list,
            onClick = {
                if (state.currentValue == SwipeAnchor.Closed) {
                    onClick()
                } else {
                    scope.launch { state.animateTo(SwipeAnchor.Closed) }
                }
            },
            onIconClick = onIconClick,
            modifier = Modifier
                .offset { IntOffset(x = state.requireOffset().roundToInt(), y = 0) }
                .anchoredDraggable(state, Orientation.Horizontal),
        )
    }
}

@Composable
private fun SwipeActionsBackground(
    fullSwipe: Boolean,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (fullSwipe) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_lists_delete_icon),
                contentDescription = stringResource(R.string.lists_action_delete),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(end = Dimens.padding24)
                    .size(Dimens.icon24),
            )
        }
    } else {
        Row(
            modifier = modifier.padding(end = Dimens.padding8),
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding8, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwipeActionButton(
                iconRes = R.drawable.ic_lists_edit_icon,
                contentDescription = stringResource(R.string.lists_action_rename),
                onClick = onRename,
            )
            SwipeActionButton(
                iconRes = R.drawable.ic_lists_duplicate_icon,
                contentDescription = stringResource(R.string.lists_action_duplicate),
                onClick = onDuplicate,
            )
            SwipeActionButton(
                iconRes = R.drawable.ic_lists_delete_icon,
                contentDescription = stringResource(R.string.lists_action_delete),
                onClick = onDelete,
            )
        }
    }
}

@Composable
private fun SwipeActionButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(Dimens.icon48)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimens.icon24),
        )
    }
}

@AppPreview
@Composable
private fun SwipeableListCardPreview() {
    AppTheme {
        SwipeableListCard(
            list = ShoppingList(
                id = 1L,
                userId = 1L,
                name = "Продукты",
                iconKey = "shopping_cart",
                sortMode = SortMode.MANUAL,
            ),
            onClick = {},
            onIconClick = {},
            onRename = {},
            onDuplicate = {},
            onDelete = {},
        )
    }
}
