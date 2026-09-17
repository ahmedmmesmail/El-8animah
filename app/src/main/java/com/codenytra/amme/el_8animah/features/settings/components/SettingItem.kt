package com.codenytra.amme.el_8animah.features.settings.components

import androidx.compose.ui.graphics.vector.ImageVector

enum class SettingsTrailing {
    CHEVRON,
    SWITCH,
    VALUE
}

data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val trailing: SettingsTrailing = SettingsTrailing.CHEVRON,
    val value: String = "",
    val checked: Boolean = false,
    val onToggle: (Boolean) -> Unit = {},
    val onClick: () -> Unit = {}
)