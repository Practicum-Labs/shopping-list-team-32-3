package ru.practicum.shoppinglist.feature.auth.ui.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.feature.auth.ui.RegisterContract
import ru.practicum.shoppinglist.feature.auth.ui.RegisterViewModelBase

class RegisterViewModelMock(initial: RegisterContract.State) :
    RegisterViewModelBase(initial) {
    override fun onIntent(intent: RegisterContract.Intent) {}
}

class RegisterPreviewProvider : PreviewParameterProvider<RegisterViewModelBase> {
    override val values = sequenceOf(
        RegisterViewModelMock(
            RegisterContract.State(
                isLoading = true,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                repeat = TextFieldState("repeat"),
                emailErrorId = null,
                passwordErrorId = null,
                repeatErrorId = null,
                totalError = null,
                registerEnabled = true
            )
        ),
        RegisterViewModelMock(
            RegisterContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                repeat = TextFieldState("repeat"),
                emailErrorId = R.string.auth_email_supporting_label,
                passwordErrorId = R.string.auth_password_supporting_long,
                repeatErrorId = R.string.auth_register_repeat_error,
                totalError = null,
                registerEnabled = false
            )
        ),
        RegisterViewModelMock(
            RegisterContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                repeat = TextFieldState("repeat"),
                emailErrorId = null,
                passwordErrorId = null,
                repeatErrorId = null,
                totalError = "Some error",
                registerEnabled = true
            )
        ),

    )
}
