# Settings & Modular Dashboard Feature

A modular, scalable, and responsive Settings Architecture built entirely with **Jetpack Compose** and **Material 3**. It organizes app settings into grouped sections with custom cards, badges, trailing values, and smooth animations.

---

## 🌟 Key Features

- 📑 **Grouped Sections**: Card-based visual groupings (`General`, `Data & Backup`, `Support`, `About`).
- 🧩 **Reusable UI Components**:
  - `SettingsSection`: Container card with rounded corners and section header.
  - `SettingsRow`: Rich item with icon container, title, subtitle, badges, trailing value, and click actions.
  - `SettingItem`: Clean item data model for declarative list rendering.
- 🔔 **Notification Permission Management**: Integrated `PermissionHelper` for seamless Android 13+ (API 33+) runtime notification permission requests.
- 🔗 **External Actions & Deep Links**: Rate App on Google Play, Share App, Privacy Policy URL intents, and more.

---

## 📁 File Structure

```text
features/settings/
├── PermissionHelper.kt        # Runtime permission utilities (Notification on Android 13+)
├── SettingsActivity.kt        # Main Compose settings screen assembling all features
└── components/
    ├── SettingItem.kt         # Data classes for declarative settings items
    ├── SettingsRow.kt          # Composable row item with icon, title, subtitle & value
    └── SettingsSection.kt      # Composable card grouping multiple rows together
```

---

## 🚀 How to Implement in Your Own Project

### 1. Reusable Component: `SettingsSection.kt`

```kotlin
@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 1.dp
        ) {
            Column(content = content)
        }
    }
}
```

---

### 2. Reusable Component: `SettingsRow.kt`

```kotlin
@Composable
fun SettingsRow(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    value: String? = null,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
```

---

### 3. Assembling `SettingsActivity.kt`

```kotlin
@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            SettingsSection(title = stringResource(R.string.settings_section_general)) {
                SettingsRow(
                    title = stringResource(R.string.settings_appearance),
                    subtitle = stringResource(R.string.settings_appearance_desc),
                    icon = Icons.Rounded.Palette,
                    onClick = { context.startActivity(Intent(context, AppearanceActivity::class.java)) }
                )
                HorizontalDivider()
                SettingsRow(
                    title = stringResource(R.string.settings_language),
                    subtitle = stringResource(R.string.settings_language_desc),
                    icon = Icons.Rounded.Language,
                    onClick = { /* Open LanguagePickerSheet */ }
                )
            }
        }

        item {
            SettingsSection(title = stringResource(R.string.settings_section_data)) {
                SettingsRow(
                    title = stringResource(R.string.settings_backup),
                    subtitle = stringResource(R.string.settings_backup_desc),
                    icon = Icons.Rounded.Backup,
                    onClick = { /* Trigger Backup */ }
                )
            }
        }
    }
}
```
