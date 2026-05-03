package com.example.ivorypiano.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Contains preview annotations for the three screen sizes in both light and dark themes.
 */

@Preview(name = "Compact - Light", device = "spec:width=411dp,height=891dp", showBackground = true)
@Preview(
    name = "Compact - Dark",
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(name = "Medium - Light", device = "spec:width=673dp,height=841dp", showBackground = true)
@Preview(
    name = "Medium - Dark",
    device = "spec:width=673dp,height=841dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Expanded - Light",
    device = "spec:width=1280dp,height=800dp",
    showBackground = true
)
@Preview(
    name = "Expanded - Dark",
    device = "spec:width=1280dp,height=800dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class DevicePreviews
