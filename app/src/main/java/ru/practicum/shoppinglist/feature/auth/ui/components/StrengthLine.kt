package ru.practicum.shoppinglist.feature.auth.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordStrength

@Composable
fun StrengthLine(
    state: MutableState<PasswordStrength>,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by
        animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
    val color: MutableState<Color> = remember { mutableStateOf(Color.Transparent) }
    val colorScheme = MaterialTheme.colorScheme

    var strengthText by remember { mutableStateOf(0) }

    LaunchedEffect(state.value) {
        when (state.value) {
            PasswordStrength.NONE -> {
                progress = 0f
                color.value = Color.Transparent
                strengthText = 0
            }
            PasswordStrength.WEAK -> {
                progress = 0.333f
                color.value = colorScheme.error
                strengthText = R.string.auth_password_strength_weak
            }
            PasswordStrength.MEDIUM -> {
                progress = 0.666f
                color.value = colorScheme.primary
                strengthText = R.string.auth_password_strength_medium
            }
            PasswordStrength.STRONG -> {
                progress = 1f
                color.value = colorScheme.primaryFixed
                strengthText = R.string.auth_password_strength_strong
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.padding6)
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.border6),
            color = color.value,
            trackColor = Color.LightGray,
            gapSize = -Dimens.padding12,
            drawStopIndicator = {}
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.auth_password_strength_label),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelSmall

            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(strengthText),
                color = color.value,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@AppPreview
@Composable
private fun StrengthLinePreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .padding(Dimens.padding16)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
        ) {
            StrengthLine(state = remember { mutableStateOf(PasswordStrength.NONE) })
            StrengthLine(state = remember { mutableStateOf(PasswordStrength.WEAK) })
            StrengthLine(state = remember { mutableStateOf(PasswordStrength.MEDIUM) })
            StrengthLine(state = remember { mutableStateOf(PasswordStrength.STRONG) })
        }
    }
}
