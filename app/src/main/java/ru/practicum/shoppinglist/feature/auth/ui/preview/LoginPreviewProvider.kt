package ru.practicum.shoppinglist.feature.auth.ui.preview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.feature.auth.ui.LoginContract
import ru.practicum.shoppinglist.feature.auth.ui.LoginViewModelBase

class LoginViewModelMock(initial: LoginContract.State) :
    LoginViewModelBase(initial) {
    override fun onIntent(intent: LoginContract.Intent) {}
}

class LoginPreviewProvider : PreviewParameterProvider<LoginViewModelBase> {
    override val values = sequenceOf(
        LoginViewModelMock(
            LoginContract.State(
                isLoading = true,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                emailErrorId = null,
                passwordErrorId = null,
                totalError = null,
                enterEnabled = true
            )
        ),
        LoginViewModelMock(
            LoginContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                emailErrorId = null,
                passwordErrorId = null,
                totalError = null,
                enterEnabled = true
            )
        ),
        LoginViewModelMock(
            LoginContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                emailErrorId = R.string.auth_email_supporting_label,
                passwordErrorId = R.string.auth_password_supporting_long,
                totalError = null,
                enterEnabled = false
            )
        ),
        LoginViewModelMock(
            LoginContract.State(
                isLoading = false,
                email = TextFieldState("email"),
                password = TextFieldState("password"),
                emailErrorId = null,
                passwordErrorId = null,
                totalError = "Ошибка",
                enterEnabled = false
            )
        )
    )
}
