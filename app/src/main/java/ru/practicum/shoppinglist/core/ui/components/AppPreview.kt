package ru.practicum.shoppinglist.core.ui.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview(name = "Small Screen", device = Devices.NEXUS_5, showSystemUi = true)
@Preview(device = "spec:width=600dp,height=900dp,dpi=240", name = "Medium Vertical")
@Preview(device = "spec:width=900dp,height=600dp,dpi=240", name = "Medium Horizontal")
@Preview(device = "spec:width=840dp,height=1200dp,dpi=240", name = "Large Vertical")
@Preview(device = "spec:width=1200dp,height=840dp,dpi=240", name = "Large Horizontal")
annotation class AppPreview
