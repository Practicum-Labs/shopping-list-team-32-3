package ru.practicum.shoppinglist.core.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class DropdownTextFieldPreviewModel(
    val value: String,
    val readOnly: Boolean,
    val exposed: Boolean

)

class DropdownTextFieldPreviewProvider : PreviewParameterProvider<DropdownTextFieldPreviewModel> {
    override val values = sequenceOf(
        DropdownTextFieldPreviewModel(
            value = "",
            readOnly = false,
            exposed = false
        ),
        DropdownTextFieldPreviewModel(
            value = "it",
            readOnly = false,
            exposed = false
        ),
        DropdownTextFieldPreviewModel(
            value = "it",
            readOnly = true,
            exposed = false
        ),
        DropdownTextFieldPreviewModel(
            value = "it",
            readOnly = true,
            exposed = true
        ),
        DropdownTextFieldPreviewModel(
            value = "it",
            readOnly = false,
            exposed = true
        ),
    )
}
