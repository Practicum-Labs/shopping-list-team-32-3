package ru.practicum.shoppinglist.feature.auth.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.components.DividerWithText
import ru.practicum.shoppinglist.core.ui.components.PrimaryButton
import ru.practicum.shoppinglist.core.ui.components.SecondaryButton
import ru.practicum.shoppinglist.core.ui.components.TextButton
import ru.practicum.shoppinglist.core.ui.components.TopAppBar
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.auth.ui.components.AuthGreeting

@Composable
fun LoginScreen(
    onNavigateToRegistration: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToLists: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                stringResource(R.string.auth_login_screen_title)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = Dimens.padding24)

        ) {
            AuthGreeting(
                R.drawable.image_auth_login,
                R.string.auth_login_title,
                R.string.auth_login_description,
                modifier = Modifier.padding(Dimens.padding20)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
            ) {
                AppTextField(
                    value = "",
                    onValueChange = {},
                    labelId = R.string.auth_email_label,
                    placeholderId = R.string.auth_email_label,
                    modifier = Modifier.fillMaxWidth()
                )
                AppTextField(
                    value = "",
                    onValueChange = {},
                    labelId = R.string.auth_password_label,
                    placeholderId = R.string.auth_password_label,
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryButton(
                    R.string.auth_login_enter_title,
                    onNavigateToLists,
                    modifier = Modifier.fillMaxWidth()
                )
                DividerWithText(R.string.auth_login_divider_label)
                SecondaryButton(
                    R.string.auth_register_title,
                    onNavigateToRegistration,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    R.string.auth_login_recovery_title,
                    onClick = onNavigateToRecovery,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@AppPreview
@Composable
fun LoginScreenPreview(){
    AppTheme {
        LoginScreen({},{},{})
    }
}