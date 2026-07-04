package ru.practicum.shoppinglist.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.core.ui.utils.BottomInsetFiller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    onDismiss: () -> Unit,
    fab: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box {
        Scrim(onDismiss)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                fab?.let {
                    Row(modifier = Modifier.padding(end = Dimens.padding16, bottom = Dimens.padding32)) {
                        Spacer(modifier = Modifier.weight(1f))
                        it()
                    }
                }
                BottomSheetContent(onDismiss) {
                    content.invoke()
                }
            }
        }
    }
}

@Composable
private fun Scrim(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        Spacer(modifier = Modifier.weight(1f))

        BottomInsetFiller(MaterialTheme.colorScheme.surfaceContainerLow)
    }
}

@Composable
private fun BottomSheetContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val height = remember { mutableIntStateOf(0) }
    val offsetY = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .onSizeChanged { intSize: IntSize ->
                height.intValue = intSize.height
            }
            .offset(y = offsetY.value),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BottomSheetDragHandle(
                height.intValue,
                onDrag = {
                    offsetY.value = with(density) {
                        it.toDp()
                    }
                },
                onDismiss
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

@Composable
private fun BottomSheetDragHandle(
    maxHeight: Int,
    onDrag: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var offsetY by remember { mutableStateOf(0) }
    LaunchedEffect(offsetY) {
        if (offsetY in 0..maxHeight) {
            onDrag(offsetY)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        offsetY += dragAmount.y.toInt()

                        change.consumeAllChanges()
                    },
                    onDragEnd = {
                        if (offsetY > maxHeight / 2) {
                            onDismiss()
                        } else {
                            offsetY = 0
                        }
                    },
                    onDragCancel = {
                        offsetY = 0
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(MaterialTheme.colorScheme.outline)

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@AppPreview
@Composable
private fun ModalBottomSheetPreview() {
    AppTheme {
        AppModalBottomSheet({}, fab = { AddFab({}) }) {
            Text("Some Content 2")
        }
    }
}
