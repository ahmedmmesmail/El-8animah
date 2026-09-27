package com.codenytra.amme.el_8animah.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ---------- Dark ----------
private val DarkColorScheme = darkColorScheme(
    primary = LightGold,
    onPrimary = Color(0xFF1C1C1C),
    primaryContainer = Color(0xFF4A3B12),
    onPrimaryContainer = Color(0xFFFFE082),

    secondary = SandyYellow,
    onSecondary = Color(0xFF1C1C1C),
    secondaryContainer = Color(0xFF45411A),
    onSecondaryContainer = Color(0xFFFFF59D),

    tertiary = Blueberry,
    onTertiary = Color(0xFFFFD54F),
    tertiaryContainer = Color(0xFF55246A),
    onTertiaryContainer = Color(0xFFF3D9FA),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE6E6E6),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE6E6E6),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFC4C4C4),
    outline = Color(0xFF8A8A8A)
)

// ---------- Light ----------
private val LightColorScheme = lightColorScheme(
    primary = Tangerine,
    onPrimary = Color(0xFF1C1C1C),
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFF3A2A12),

    secondary = Peach,
    onSecondary = Color(0xFF1C1C1C),
    secondaryContainer = Color(0xFFFFF59D),
    onSecondaryContainer = Color(0xFF33300F),

    tertiary = LightBlue,
    onTertiary = Color(0xFFFFB300),
    tertiaryContainer = LightBlue2,
    onTertiaryContainer = Color(0xFF001F2B),

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1C1C),
    surface = Color(0xFFFAFAFA),
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFE8E8E8),
    onSurfaceVariant = Color(0xFF4A4A4A),
    outline = Color(0xFF7A7A7A)
)

@Composable
fun El8animahTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}