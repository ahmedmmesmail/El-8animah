package com.codenytra.amme.el_8animah.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightGold = Color(0xFFFFD54F)
val SandyYellow = Color(0xFFFFF176)
val Blueberry = Color(0xFF4F378B)
val Tangerine = Color(0xFFFB8C00)
val Peach = Color(0xFFFAA781)
val LightBlue = Color(0xFF00BFFF)
val LightBlue2 = Color(0xFF81D4FA)


object CustomColors {
    var black = false

    val topBarColors: TopAppBarColors
        @Composable get() = TopAppBarDefaults.topAppBarColors(
            containerColor = if (!black)
                colorScheme.surfaceContainer
            else
                colorScheme.surface,
            scrolledContainerColor = if (!black)
                colorScheme.surfaceContainer
            else
                colorScheme.surface
        )

    val detailPaneTopBarColors: TopAppBarColors
        @Composable get() = TopAppBarDefaults.topAppBarColors(
            containerColor = if (!black)
                colorScheme.surfaceContainerLow
            else
                colorScheme.surface,
            scrolledContainerColor = if (!black)
                colorScheme.surfaceContainerLow
            else
                colorScheme.surface
        )

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    val listItemColors: ListItemColors
        @Composable get() = ListItemDefaults.colors(
            containerColor = if (!black)
                colorScheme.surfaceBright
            else
                colorScheme.surfaceContainerHigh
        )

    val switchColors: SwitchColors
        @Composable get() = SwitchDefaults.colors(
            checkedIconColor = colorScheme.primary,
        )
}
