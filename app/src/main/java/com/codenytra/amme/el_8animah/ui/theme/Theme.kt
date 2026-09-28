package com.codenytra.amme.el_8animah.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.codenytra.amme.el_8animah.features.appearance.ElGhanimahApp
import com.codenytra.amme.el_8animah.features.appearance.ThemeMode
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme

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
    // مش محتاج parameters تاني
    // بياخد البيانات من ThemeViewModel مباشرة
    content: @Composable () -> Unit
) {
    // ─── نقرأ من ThemeViewModel ──────────────────────────
    // DeTauroApplication.instance = الـ Application singleton
    // .themeViewModel = الـ ViewModel الوحيد في التطبيق
    //
    // لما الـ ViewModel state يتغير (في AppearanceActivity مثلاً)
    // Compose يلاحظ التغيير ويعيد رسم أي composable بيقرأ من الـ state دي
    // ── حتى لو كانت في Activity مختلفة تماماً ──
    val vm = ElGhanimahApp.instance.themeViewModel

    // بنقرأ الـ state من الـ VM — الـ properties دي Compose State
    // يعني Compose مسجّل عليها ومنتظر أي تغيير
    val systemDark  = isSystemInDarkTheme()
    val darkTheme = when (vm.themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT  -> false
        ThemeMode.DARK   -> true
    }
    val dynamicColor = vm.dynamicColor
    val blackTheme   = vm.blackTheme
    val seedColor    = vm.seedColor ?: Color.White

    // ─── نبني الـ color scheme ───────────────────────────
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    // ─── Status bar ──────────────────────────────────────
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CustomColors.black = blackTheme && darkTheme

    // ─── rememberDynamicColorScheme من materialkolor ─────
    // بيبني color scheme من seed color واحد
    // كل ما seedColor أو darkTheme يتغير → بيعيد الحساب
    val dynamicColorScheme = rememberDynamicColorScheme(
        seedColor = when (seedColor) {
            Color.White -> colorScheme.primary  // مش في لون مختار = الـ primary الافتراضي
            else        -> seedColor
        },
        isDark = darkTheme,
        specVersion = if (blackTheme && darkTheme)
            ColorSpec.SpecVersion.SPEC_2021 else ColorSpec.SpecVersion.SPEC_2025,
        isAmoled = blackTheme && darkTheme
    )

    // ─── اختيار الـ scheme النهائي ───────────────────────
    // لو مفيش seed color مختار ومش black theme = نستخدم الـ static scheme
    // غير كده = نستخدم الـ dynamic scheme المبني من الـ seed
    val scheme = if (seedColor == Color.White && !(blackTheme && darkTheme))
        colorScheme
    else
        dynamicColorScheme

    MaterialExpressiveTheme(
        colorScheme = scheme,
        typography   = Typography,
        motionScheme = MotionScheme.expressive(),
        content      = content
    )
}