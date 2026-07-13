package ru.practicum.shoppinglist.feature.auth.ui.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.feature.auth.ui.RecoveryContract
import ru.practicum.shoppinglist.feature.auth.ui.RecoveryViewModelBase

class RecoveryViewModelMock(initial: RecoveryContract.State) :
    RecoveryViewModelBase(initial) {
    override fun onIntent(intent: RecoveryContract.Intent) {}
}

class RecoveryPreviewProvider : PreviewParameterProvider<RecoveryViewModelBase> {
    override val values = sequenceOf(
        RecoveryViewModelMock(
            RecoveryContract.State(
                isLoading = true,
                email = TextFieldState("email@email.ru"),
                emailErrorId = null,
                totalError = null,
                recoveryEnabled = true,
                success = false
            )
        ),
        RecoveryViewModelMock(
            RecoveryContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                emailErrorId = R.string.auth_email_supporting_label,
                totalError = null,
                recoveryEnabled = false,
                success = false
            )
        ),
        RecoveryViewModelMock(
            RecoveryContract.State(
                isLoading = false,
                email = TextFieldState("email@email.ru"),
                emailErrorId = null,
                totalError = "Some error",
                recoveryEnabled = true,
                success = false
            )
        ),
        RecoveryViewModelMock(
            RecoveryContract.State(
                isLoading = false,
                email = TextFieldState("email@email.ru"),
                emailErrorId = null,
                totalError = null,
                recoveryEnabled = true,
                success = true
            )
        ),
    )
}
