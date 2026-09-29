package com.codenytra.amme.el_8animah.features.appearance

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.outlined.AutoMode
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.rounded.AutoMode
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme

class AppearanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val vm = ElGhanimahApp.instance.themeViewModel

            El8animahTheme {
               AppearanceScreen(
                    // Pass reactive state directly from Application-level ThemeViewModel
                    themeMode = vm.themeMode,
                    dynamicColor = vm.dynamicColor,
                    blackTheme = vm.blackTheme,
                    seedColor = vm.seedColor,
                    // Updates mutate ThemeViewModel state and persist to SharedPreferences
                    onThemeModeChange = { vm.updateThemeMode(it) },
                    onDynamicColorChange = { vm.updateDynamicColor(it) },
                    onBlackThemeChange = { vm.updateBlackTheme(it) },
                    onSeedColorChange = { vm.updateSeedColor(it) },
                    onNavigateBack = { finish() }
                )
            }
        }
    }
}

data class ColorOption(
    val color: Color,
    val labelRes: Int
)

val colorPalette = listOf(
    ColorOption(Color(0xFFFFEB3B), R.string.color_yellow),        // Green
    ColorOption(Color(0xFFFFC107), R.string.color_amber),       // Amber دافئ
    ColorOption(Color(0xFFFF9800), R.string.color_orange),      // Orange صريح
    ColorOption(Color(0xFFFF5722), R.string.color_soft_red),    // Red متوازن
    ColorOption(Color(0xFFF44336), R.string.color_red),        // Pink حيوي
    ColorOption(Color(0xFFE91E63), R.string.color_pink),        // Pink حيوي

    ColorOption(Color(0xFF9C27B0), R.string.color_purple),      // Purple ملكي
    ColorOption(Color(0xFF673AB7), R.string.color_indigo),      // Indigo عميق
    ColorOption(Color(0xFF3F51B5), R.string.color_blue),        // Blue أساسي
    ColorOption(Color(0xFF2196F3), R.string.color_cyan),        // Cyan منعش
    ColorOption(Color(0xFF009688), R.string.color_teal),        // Teal احترافي


    ColorOption(Color(0xFF4CAF50), R.string.color_green),       // Green طبيعي
    ColorOption(Color(0xFF8BC34A), R.string.color_lime),        // Lime عصري
    ColorOption(Color(0xFFCDDC39), R.string.color_chartreuse),  // Chartreuse
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    themeMode: ThemeMode,
    dynamicColor: Boolean,
    blackTheme: Boolean,
    seedColor: Color?,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onBlackThemeChange: (Boolean) -> Unit,
    onSeedColorChange: (Color?) -> Unit,
    onNavigateBack: () -> Unit = {}
) {

    // Dynamic color (Material You wallpaper extraction) requires Android 12+ (API 31+)
    val supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            stringResource(R.string.appearance_title),
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            stringResource(R.string.appearance_settings_label),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                },
                navigationIcon = {
                    // زر Back - بيظهر زي الصور تماماً (دايرة بـ background)
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.size(18.dp).clip(MaterialTheme.shapes.medium)
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                // بنزود الـ padding بتاع العنوان عشان يبعد عن البداية
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp, top = 16.dp, bottom = 40.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ─── Settings Card ────────────────────────────────
            // بنحط كل الـ settings في card واحد كبير
            // بالظبط زي الصور - cards بـ rounded corners
            item {
                AppearanceCard {

                    // ── 1. Theme Mode ──────────────────────────
                    ThemeSection(
                        currentMode = themeMode,
                        onModeChange = onThemeModeChange
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )

                    // ── 2. Dynamic Color ───────────────────────
                    // بيظهر بس لو الجهاز بيدعمه (Android 12+)
                    if (supportsDynamicColor) {
                        DynamicColorSection(
                            enabled = dynamicColor,
                            onToggle = { enabled ->
                                onDynamicColorChange(enabled)
                                // لو فعّل Dynamic Color = نشيل الـ seed color المختار
                                // عشان Dynamic Color بياخد لون الـ wallpaper تلقائياً
                                if (enabled) onSeedColorChange(null)
                            }
                        )

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }

                    // ── 3. Color Scheme ────────────────────────
                    ColorSchemeSection(
                        selectedColor = seedColor,
                        dynamicEnabled = dynamicColor,
                        onColorSelect = { color ->
                            onSeedColorChange(color)
                            if (dynamicColor) onDynamicColorChange(false)
                        }
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // ── 4. Black Theme ─────────────────────────
                    // بيظهر بس لو الثيم داكن (System/Dark)
                    BlackThemeSection(
                        enabled = blackTheme,
                        onToggle = onBlackThemeChange
                    )
                }
            }
        }
    }
}

// ============================================================
// APPEARANCE CARD - الـ card الرئيسي
// ============================================================
// بنعمل wrapper composable عشان نستخدم نفس الـ style مرة واحدة
@Composable
private fun AppearanceCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            content()
        }
    }
}

// ============================================================
// SECTION ROW - template عام لكل section
// ============================================================
@Composable
private fun SectionRow(
    icon: @Composable () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    below: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon wrapper for better expressive look
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
            trailing?.invoke()
        }
        below?.invoke()
    }
}

// ============================================================
// 1. THEME SECTION
// ============================================================
@Composable
private fun ThemeSection(
    currentMode: ThemeMode,
    onModeChange: (ThemeMode) -> Unit
) {
    SectionRow(
        icon = {
            // الأيقونة بتتغير حسب الثيم الحالي - بالظبط زي الصور
            val icon = when (currentMode) {
                ThemeMode.SYSTEM -> Icons.Rounded.AutoMode      // شمس + قمر
                ThemeMode.LIGHT -> Icons.Rounded.LightMode     // شمس
                ThemeMode.DARK -> Icons.Rounded.DarkMode      // قمر
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        title = stringResource(R.string.appearance_theme_label),
        below = {
            Spacer(Modifier.height(12.dp))
            //= الـ 3 أزرار (System / Light / Dark)
            ThemeConnectedButtonGroup(
                current = currentMode,
                onSelect = onModeChange
            )
        }
    )
}

// ============================================================
// THEME SEGMENTED BUTTON
// ============================================================
// بنعمله custom عشان يبدو بالظبط زي الصور
// مش بنستخدم SegmentedButton من Material 3 عشان التصميم مختلف
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemeConnectedButtonGroup(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    val options = listOf(
        ThemeMode.SYSTEM to stringResource(R.string.appearance_theme_system),
        ThemeMode.LIGHT to stringResource(R.string.appearance_theme_light),
        ThemeMode.DARK to stringResource(R.string.appearance_theme_dark)
    )

    val unCheckedIcons = listOf(
        Icons.Outlined.AutoMode,
        Icons.Outlined.LightMode,
        Icons.Outlined.DarkMode
    )

    val checkedIcons = listOf(
        Icons.Rounded.AutoMode,
        Icons.Rounded.LightMode,
        Icons.Rounded.DarkMode
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(
            ButtonGroupDefaults.ConnectedSpaceBetween
        ),
    ) {

        options.forEachIndexed { index, (mode, label) ->

            val isSelected = current == mode

            ToggleButton(
                checked = isSelected,
                onCheckedChange = { onSelect(mode) },
                modifier = Modifier
                    .weight(1f)
                    .semantics { role = Role.RadioButton },

                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                }
            ) {

                Icon(
                    if (isSelected) checkedIcons[index] else unCheckedIcons[index],
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )

                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))

                Text(
                    label,
                    style = TextStyle(
                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

// ============================================================
// 2. DYNAMIC COLOR SECTION
// ============================================================
@Composable
private fun DynamicColorSection(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    SectionRow(
        icon = {
            Icon(
                Icons.Rounded.FormatPaint,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        title = stringResource(R.string.appearance_dynamic_color),
        trailing = {
            Switch(
                checked = enabled,
                onCheckedChange = onToggle
            )
        },
        below = {
            Spacer(Modifier.height(2.dp))
            Text(
                stringResource(R.string.appearance_dynamic_color_desc),
                style = TextStyle(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            )
        }
    )
}

// ============================================================
// 3. COLOR SCHEME SECTION
// ============================================================
@Composable
private fun ColorSchemeSection(
    selectedColor: Color?,
    dynamicEnabled: Boolean,
    onColorSelect: (Color?) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ─── Header ──────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Palette,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.appearance_color_scheme),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    // بنوضح للمستخدم إيه الحالة الحالية
                    text = when {
                        dynamicEnabled -> stringResource(R.string.appearance_color_dynamic)
                        selectedColor == null -> stringResource(R.string.appearance_color_default)
                        // بندور على اسم اللون المختار من الـ palette
                        else -> colorPalette.find { it.color == selectedColor }?.let { stringResource(it.labelRes) } ?: stringResource(
                            R.string.appearance_color_custom)
                    },
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // ─── Color Circles ───────────────────────────────────
        // horizontalScroll = بيخلي الألوان تتمرر أفقياً
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            colorPalette.forEach { option ->
                // هل اللون ده هو المختار؟
                val isSelected = !dynamicEnabled && selectedColor == option.color

                ColorCircle(
                    color = option.color,
                    isSelected = isSelected,
                    onClick = {
                        // لو ضغط على نفس اللون المختار = نرجع للـ default
                        if (isSelected) {
                            onColorSelect(null)
                        } else {
                            onColorSelect(option.color)
                        }
                    }
                )
            }
        }
    }
}

// ============================================================
// COLOR CIRCLE - دايرة اللون
// ============================================================
@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(MaterialTheme.shapes.medium)
            // لو selected = border أبيض/أسود محيط بيه
            .then(
                if (isSelected) Modifier.border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = MaterialTheme.shapes.medium
                ) else Modifier
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // الدايرة الملونة - أصغر شوية من الـ Box عشان نخلي فراغ للـ border
        Box(
            modifier = Modifier
                .size(if (isSelected) 42.dp else 50.dp)  // تصغر لو selected عشان يظهر الـ border
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    // Use luminance (0.0=black to 1.0=white) to guarantee visible checkmark contrast
                    tint = if (color.luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ============================================================
// 4. BLACK THEME SECTION
// ============================================================
@Composable
private fun BlackThemeSection(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    SectionRow(
        icon = {
            Icon(
                Icons.Rounded.Contrast,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        title = stringResource(R.string.appearance_black_theme),
        trailing = {
            Switch(
                checked = enabled,
                onCheckedChange = onToggle
            )
        },
        below = {
            Spacer(Modifier.height(2.dp))
            Text(
                stringResource(R.string.appearance_black_theme_desc),
                style = TextStyle(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            )
        }
    )
}
