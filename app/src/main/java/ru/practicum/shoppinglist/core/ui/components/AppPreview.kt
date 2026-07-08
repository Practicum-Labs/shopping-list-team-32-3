package ru.practicum.shoppinglist.core.ui.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(device = "spec:width=600dp,height=900dp,dpi=240", name = "Medium Screen")
@Preview(device = "spec:width=840dp,height=1200dp,dpi=240", name = "Large Screen")
annotation class AppPreview
