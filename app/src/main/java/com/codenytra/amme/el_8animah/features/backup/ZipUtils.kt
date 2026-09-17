package com.codenytra.amme.el_8animah.features.backup

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.system.exitProcess

object ZipUtils {

    private const val TAG = "ZipUtils"
    private const val EL8ANIMAH_FOLDER = "El-8animah"
    private const val FILE_EXTENSION = ".8animah"

    fun createBackupFileName(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        return "el-8animah_backup_${fmt.format(Date())}$FILE_EXTENSION"
    }

    // Public Documents/El-8animah folder, created if it doesn't exist yet
    fun getOrCreateFolder(): File {
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            EL8ANIMAH_FOLDER
        )
        if (!folder.exists()) folder.mkdirs()
        return folder
    }

    // Creates a new backup file in the Documents folder. Returns null on failure.
    fun saveBackupToFolder(context: Context): File? {
        return try {
            val outFile = File(getOrCreateFolder(), createBackupFileName())
            FileOutputStream(outFile).use { fos ->
                ZipOutputStream(BufferedOutputStream(fos)).use { zos ->
                    zipAppDataToStream(context, zos)
                }
            }
            Log.d(TAG, "Backup saved: ${outFile.absolutePath}")
            outFile
        } catch (e: Exception) {
            Log.e(TAG, "Backup failed: ${e.message}")
            null
        }
    }

    /*    // Same as saveBackupToFolder but writes to a user-picked Uri (e.g. from a file picker)
    fun zipSharedPreferencesToUri(context: Context, outputUri: Uri) {
        try {
            context.contentResolver.openOutputStream(outputUri)?.use { os ->
                ZipOutputStream(BufferedOutputStream(os)).use { zos ->
                    zipAppDataToStream(context, zos)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Backup to URI failed: ${e.message}")
        }
    }*/

    // Skip Google/Firebase related files so restoring a backup doesn't
    // overwrite device-specific Google services data (e.g. install IDs, ad IDs)
    private fun shouldSkipFile(fileName: String): Boolean {
        val lower = fileName.lowercase()
        return lower.contains("firebase") ||
                lower.contains("gms") ||
                lower.contains("google")
    }

    // Zips the app's shared_prefs and databases folders into the given ZipOutputStream
    private fun zipAppDataToStream(context: Context, zos: ZipOutputStream) {
        val sharedPrefsDir = File(context.filesDir.parent, "shared_prefs")
        val databasesDir = File(context.filesDir.parent, "databases")

        // Zip Shared Prefs
        if (sharedPrefsDir.exists()) {
            for (file in sharedPrefsDir.listFiles() ?: emptyArray()) {
                if (shouldSkipFile(file.name)) continue
                FileInputStream(file).use { fis ->
                    zos.putNextEntry(ZipEntry("shared_prefs/${file.name}"))
                    fis.copyTo(zos)
                    zos.closeEntry()
                }
            }
        }

        // Zip Databases
        if (databasesDir.exists()) {
            for (file in databasesDir.listFiles() ?: emptyArray()) {
                if (shouldSkipFile(file.name)) continue
                FileInputStream(file).use { fis ->
                    zos.putNextEntry(ZipEntry("databases/${file.name}"))
                    fis.copyTo(zos)
                    zos.closeEntry()
                }
            }
        }
    }

    /*
    // ──────────────────────────────────────────────────────
    // restoreBackupFromFile
    //
    // Writes the backup files to disk, then restarts the app.
    //
    // Why restart instead of just reloading?
    // ─────────────────────────────
    // Android caches SharedPreferences in RAM. Even after we write
    // the new files to disk, the old in-memory cache won't refresh.
    // The only reliable fix is to kill the process and relaunch it.
    // ──────────────────────────────────────────────────────
    fun restoreBackupFromFile(context: Context, backupFile: File): Boolean {
        return try {
            // 1. Write the backup files to disk
            FileInputStream(backupFile).use { fis ->
                unzipAppData(context, fis)
            }
            Log.d(TAG, "Restore written to disk: ${backupFile.name}")

            // 2. Restart the app so the new data is picked up
            restartApp(context)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Restore failed: ${e.message}")
            false
        }
    }
*/

    // Same restore flow as restoreBackupFromFile, but reads from a Uri instead of a File
    fun unzipSharedPreferencesFromUri(context: Context, inputUri: Uri) {
        try {
            context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
                unzipAppData(context, inputStream)
            }
            restartApp(context)
        } catch (e: Exception) {
            Log.e(TAG, "Restore from URI failed: ${e.message}")
        }
    }

    private fun unzipAppData(
        context: Context,
        inputStream: InputStream
    ) {
        ZipInputStream(BufferedInputStream(inputStream)).use { zis ->
            val appDir = context.filesDir.parent ?: return

            var entry = zis.nextEntry
            while (entry != null) {
                val fileName = entry.name.substringAfterLast('/')
                if (shouldSkipFile(fileName)) {
                    zis.closeEntry()
                    entry = zis.nextEntry
                    continue
                }

                val outputFile = File(appDir, entry.name)

                // Ensure parent directory exists
                outputFile.parentFile?.let {
                    if (!it.exists()) it.mkdirs()
                }

                // Overwrite any existing file with the restored one
                if (outputFile.exists()) outputFile.delete()
                FileOutputStream(outputFile).use { fos -> zis.copyTo(fos) }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
    }

    // ──────────────────────────────────────────────────────
    // restartApp
    //
    // Steps:
    // 1. AlarmManager schedules an Intent to open MainActivity after 600ms
    // 2. exitProcess(0) kills the current process immediately
    // 3. After 600ms the OS relaunches the app
    //    → it reads the new SharedPreferences from disk
    //
    // Why AlarmManager instead of Handler.postDelayed?
    // Because exitProcess kills everything, including the Handler.
    // AlarmManager runs at the system level, not inside our process.
    //
    // Why FLAG_IMMUTABLE?
    // Required on Android 12+ (API 31+)
    // ──────────────────────────────────────────────────────
    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(context.packageName)!!
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, launchIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + 600,
            pendingIntent
        )

        exitProcess(0)
    }

    /*
    // Lists all backup files, newest first
    fun listBackups(): List<File> {
        return getOrCreateFolder()
            .listFiles { file -> file.isFile && file.name.endsWith(FILE_EXTENSION) }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }
*/
}