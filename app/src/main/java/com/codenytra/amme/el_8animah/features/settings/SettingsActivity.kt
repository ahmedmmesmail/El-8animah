package com.codenytra.amme.el_8animah.features.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.codenytra.amme.el_8animah.BuildConfig
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codenytra.amme.el_8animah.BaseActivity
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.features.backup.BackupResultDialog
import com.codenytra.amme.el_8animah.features.backup.ResetDialog
import com.codenytra.amme.el_8animah.features.backup.ZipUtils
import com.codenytra.amme.el_8animah.features.feecback.FeedbackDialog
import com.codenytra.amme.el_8animah.features.localization.LanguagePickerSheet
import com.codenytra.amme.el_8animah.features.localization.LocaleHelper
import com.codenytra.amme.el_8animah.features.localization.LocaleViewModel
import com.codenytra.amme.el_8animah.features.settings.components.SettingItem
import com.codenytra.amme.el_8animah.features.settings.components.SettingsSection
import com.codenytra.amme.el_8animah.features.settings.components.SettingsTrailing
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme

class SettingsActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.settings_title),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: LocaleViewModel = viewModel()
) {

    val context = LocalContext.current

    var notifications by remember {
        mutableStateOf(
            PermissionHelper.hasNotificationPermission(
                context
            )
        )
    }
    var pinLockEnabled by remember { mutableStateOf(true) }
    var showBackupResultDialog by remember { mutableStateOf<String?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showFeedbackSheet by remember { mutableStateOf(false) }

    val prefs = remember {
        context.getSharedPreferences(
            "el_8animah_pref",
            Context.MODE_PRIVATE
        )
    }

    val currentLocale = viewModel.currentLocale
    val currentLanguage = LocaleHelper.supportedLanguages
        .find { it.code == currentLocale }
        ?: LocaleHelper.supportedLanguages.first()

//     ── Refresh state on resume (after PinSetupActivity etc.) ──

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
//                pinLock = PinManager.hasPinSet(context)
                notifications = PermissionHelper.hasNotificationPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Opens a file picker so the user can choose a .8animah backup file to restore
    val restoreLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            // unzipSharedPreferencesFromUri writes files to disk then restarts the app
            ZipUtils.unzipSharedPreferencesFromUri(context, uri)
        }
    }
    // Requests WRITE_EXTERNAL_STORAGE at runtime (needed on Android 8 & 9 only,
    // see the SDK version check below). Result callback then attempts the backup.
    val storagePermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = ZipUtils.saveBackupToFolder(context)
            showBackupResultDialog = if (file != null) {
                context.getString(R.string.backup_success, file.name)
            } else {
                context.getString(R.string.backup_failed)
            }
        } else {
            showBackupResultDialog = context.getString(R.string.backup_permission_required)
        }
    }

    if (showLanguageSheet) {
        LanguagePickerSheet(
            currentCode = currentLocale,
            onSelect = { lang ->
                viewModel.updateLocale(lang.code)
                showLanguageSheet = false
                (context as? Activity)?.recreate()
            },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showFeedbackSheet) {
        FeedbackDialog(onDismiss = { showFeedbackSheet = false })
    }

    if (showResetDialog) {
        ResetDialog(
            onDismissRequest = {
                showResetDialog = false
            },
            onConfirm = {
                showResetDialog = false
                try {
                    // clearApplicationUserData wipes all app data (prefs, databases,
                    // files, cache) and restarts the app automatically, similar to
                    // "Clear Data" in Android system settings
                    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    am.clearApplicationUserData()
                } catch (_: Exception) {
                    // Fallback in case of any error: manual clear of all prefs
                    prefs.edit { clear() }
                    context.getSharedPreferences("el_8animah_locale", Context.MODE_PRIVATE)
                        .edit { clear() }

                    // Notify other parts of the app that settings were reset,
                    // since clearApplicationUserData wasn't available to restart for us
                    context.sendBroadcast(
                        Intent("com.codenytra.amme.el_8animah.SETTINGS_CHANGED")
                            .setPackage(context.packageName)
                    )
                }
            }
        )
    }

    if (showBackupResultDialog != null) {
        // Detect success/failure from the message text itself
        // (backup_success string starts with "Backup saved")
        val isSuccess = showBackupResultDialog!!.startsWith("Backup saved")
        BackupResultDialog(
            isSuccess = isSuccess,
            showBackupResultDialog = showBackupResultDialog,
            onDismissRequest = { showBackupResultDialog = null }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        item {
            SettingsSection(
                header = stringResource(R.string.settings_section_general),
                content = listOf(
                    SettingItem(
                        icon = Icons.Rounded.Palette,
                        title = stringResource(R.string.settings_appearance),
                        subtitle = stringResource(R.string.settings_appearance_desc),
                        trailing = SettingsTrailing.CHEVRON
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Notifications,
                        title = stringResource(R.string.settings_notifications),
                        subtitle = stringResource(R.string.settings_notifications_desc),
                        trailing = SettingsTrailing.SWITCH,
                        checked = notifications,
                        onToggle = { PermissionHelper.openNotificationSettings(context) }
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Language,
                        title = stringResource(R.string.settings_language),
                        subtitle = stringResource(R.string.settings_language_desc),
                        trailing = SettingsTrailing.VALUE,
                        value = if (currentLanguage.code == "default")
                            "${currentLanguage.flag} " + stringResource(R.string.settings_language_system)
                        else "${currentLanguage.flag} ${currentLanguage.nativeName}",
                        onClick = { showLanguageSheet = true }
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Lock,
                        title = stringResource(R.string.settings_pin_lock),
                        subtitle = stringResource(R.string.settings_pin_lock_desc),
                        trailing = SettingsTrailing.SWITCH,
                        checked = pinLockEnabled,
                        onToggle = { pinLockEnabled = it }
                    )
                )
            )
        }
        item {
            SettingsSection(
                header = stringResource(R.string.settings_section_data),
                content = listOf(
                    SettingItem(
                        title = stringResource(R.string.settings_backup),
                        subtitle = stringResource(R.string.settings_backup_desc),
                        icon = Icons.Rounded.Backup,
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // Android 10+ — no permission needed for Documents folder
                                val file = ZipUtils.saveBackupToFolder(context)

                                showBackupResultDialog = if (file != null) {
                                    // Insert file.name into the %1$s placeholder
                                    context.getString(R.string.backup_success, file.name)
                                } else {
                                    context.getString(R.string.backup_failed)
                                }
                            } else {
                                // Android 8 & 9 — request WRITE_EXTERNAL_STORAGE at runtime
                                storagePermLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                    ),
                    SettingItem(
                        title = stringResource(R.string.settings_restore),
                        subtitle = stringResource(R.string.settings_restore_desc),
                        icon = Icons.Rounded.Restore,
                        onClick = { restoreLauncher.launch(arrayOf("*/*")) }
                    ),
                    SettingItem(
                        title = stringResource(R.string.settings_reset),
                        subtitle = stringResource(R.string.settings_reset_desc),
                        icon = Icons.Rounded.DeleteForever,
                        onClick = { showResetDialog = true }
                    ),
                )
            )
        }
        item {
            SettingsSection(
                header = stringResource(R.string.settings_section_support),
                content = listOf(
                    SettingItem(
                        icon = Icons.Rounded.StarRate,
                        title = stringResource(R.string.settings_rate_app),
                        subtitle = stringResource(R.string.settings_rate_app_desc),
                        trailing = SettingsTrailing.CHEVRON,
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".toUri()
                            )
                            context.startActivity(intent)
                        }
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Apps,
                        title = stringResource(R.string.settings_our_apps),
                        subtitle = stringResource(R.string.settings_our_apps_desc),
                        trailing = SettingsTrailing.CHEVRON
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Share,
                        title = stringResource(R.string.settings_share_app),
                        subtitle = stringResource(R.string.settings_share_app_desc),
                        trailing = SettingsTrailing.CHEVRON,
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
//                                    sharing text:
                                    context.getString(R.string.check_out) +
//                                    app link:
//                                    add (buildFeatures { buildConfig = true }) in build.gradle.kts
                                            "\nhttps://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                                )
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "${context.getString(R.string.settings_share)} ${
                                        context.getString(
                                            R.string.app_name
                                        )
                                    }"
                                )
                            )
                        }
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Feedback,
                        title = stringResource(R.string.settings_feedback),
                        subtitle = stringResource(R.string.settings_feedback_desc),
                        trailing = SettingsTrailing.CHEVRON,
                        onClick = { showFeedbackSheet = true }
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Policy,
                        title = stringResource(R.string.settings_privacy_policy),
                        subtitle = stringResource(R.string.settings_privacy_policy_desc),
                        trailing = SettingsTrailing.CHEVRON
                    ),
                    SettingItem(
                        icon = Icons.Rounded.Info,
                        title = stringResource(R.string.settings_about),
                        subtitle = stringResource(R.string.settings_about_desc),
                        trailing = SettingsTrailing.CHEVRON
                    )
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    El8animahTheme {
        SettingsScreen()
    }
}