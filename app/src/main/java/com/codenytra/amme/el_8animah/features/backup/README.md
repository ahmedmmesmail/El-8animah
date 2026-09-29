# Data Backup & Restore Feature

A comprehensive backup, export, restore, and reset solution for Android applications. It archives application preferences (`shared_prefs`) and SQLite/Room databases (`databases`) into a compressed `.8animah` zip archive, while intelligently filtering out device-specific runtime tokens (e.g., Firebase, Google Play Services).

---

## 🌟 Key Features

- 📦 **Full App State Archiving**: Archives all user SharedPreferences and SQLite/Room databases.
- 🛡️ **Smart Security Filtering**: Automatically excludes volatile and device-bound metadata (`firebase`, `gms`, `google`).
- 📁 **Documents Export**: Stores backups cleanly under `Documents/El-8animah/` using formatted timestamps.
- ⚡ **Seamless Hot Restart**: Restores data from disk and restarts the application via system-level `AlarmManager` and `exitProcess(0)` to invalidate in-memory `SharedPreferences` cache cleanly.
- 🔄 **Safe Reset Dialog**: Provides a two-step confirmation dialog to wipe preferences and start fresh.

---

## 📁 File Structure

```text
features/backup/
├── BackupResultDialog.kt  # Compose dialog displaying backup success/failure with file path
├── ResetDialog.kt         # Confirmation dialog for resetting all application settings
└── ZipUtils.kt            # Core engine for zipping, unzipping, filtering & app restarting
```

---

## 🚀 How to Implement in Your Own Project

### 1. Declare Permissions in `AndroidManifest.xml`

For writing to public storage on Android 9 and below:

```xml
<uses-permission
    android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />
```

*(On Android 10+ / API 29+, Scoped Storage or SAF Storage Access Framework is used).*

---

### 2. Copy `ZipUtils.kt` to your project

Key functions in `ZipUtils`:

#### A. Creating and Saving a Backup
```kotlin
val backupFile: File? = ZipUtils.saveBackupToFolder(context)
if (backupFile != null) {
    // Show success dialog with file path: backupFile.absolutePath
} else {
    // Show error toast/dialog
}
```

#### B. Restoring from a File or Uri (Storage Access Framework)
```kotlin
// In your Activity using ActivityResultContracts.OpenDocument()
val openBackupLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument()
) { uri: Uri? ->
    if (uri != null) {
        ZipUtils.unzipSharedPreferencesFromUri(context, uri)
        // Note: The app automatically restarts via AlarmManager upon completion!
    }
}

// Trigger file picker
openBackupLauncher.launch(arrayOf("*/*"))
```

#### C. Resetting All App Data
```kotlin
fun resetAllData(context: Context) {
    // 1. Clear SharedPreferences
    val sharedPrefsDir = File(context.filesDir.parent, "shared_prefs")
    if (sharedPrefsDir.exists()) {
        sharedPrefsDir.listFiles()?.forEach { it.delete() }
    }
    // 2. Restart app to refresh state
    ZipUtils.restartApp(context)
}
```

---

### 3. Displaying Results via Compose Dialogs

#### Show Backup Result Dialog:
```kotlin
if (showBackupDialog) {
    BackupResultDialog(
        filePath = backupFilePath,
        onDismiss = { showBackupDialog = false }
    )
}
```

#### Show Reset Confirmation Dialog:
```kotlin
if (showResetDialog) {
    ResetConfirmDialog(
        onConfirm = {
            showResetDialog = false
            resetAllData(context)
        },
        onDismiss = { showResetDialog = false }
    )
}
```

---

## 🔍 Technical Detail: Why `AlarmManager` for Restoring?

Android caches `SharedPreferences` in RAM per process. Overwriting the XML files on disk does **not** update the existing memory cache in the running process.

`ZipUtils` solves this cleanly:
1. Schedules an explicit `PendingIntent` for `MainActivity` 600ms in the future via `AlarmManager.RTC`.
2. Calls `exitProcess(0)` to immediately kill the process.
3. The Android OS relaunches the app fresh, reading the restored data from disk.
