package ru.practicum.shoppinglist.core.ui.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(name = "Small Screen", device = Devices.NEXUS_5, showSystemUi = true)
annotation class AppPreview
