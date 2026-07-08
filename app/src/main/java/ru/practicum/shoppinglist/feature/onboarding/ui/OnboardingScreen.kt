package ru.practicum.shoppinglist.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.EmptyState
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun OnboardingScreen(
    onNavigateToLists: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(5000L)
        onNavigateToLists()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clickable(onClick = onNavigateToLists),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = Dimens.padding16)
                    .widthIn(max = Dimens.width400)
                    ,

                verticalArrangement = Arrangement.spacedBy(
                    space = Dimens.padding94,
                    alignment = Alignment.CenterVertically
                ),

                ) {
                Image(
                    painter = painterResource(id = R.drawable.image_onboarding_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = Dimens.padding36),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
                EmptyState(
                    R.drawable.image_onboarding_empty_state,
                    R.string.onboarding_title,
                    R.string.onboarding_description,
                    modifier = Modifier.padding(horizontal = Dimens.padding44)
                )
           }
        }
    }
}

@AppPreview
@Composable
private fun OnboardingScreenPreview() {
    AppTheme {
        OnboardingScreen {}
    }
}
