package com.codenytra.amme.el_8animah.features.backup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.codenytra.amme.el_8animah.R

@Composable
fun BackupResultDialog(
    onDismissRequest: () -> Unit = {},
    isSuccess: Boolean,
    showBackupResultDialog: String?
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        icon = {
            Icon(
                imageVector = if (!isSuccess) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
                contentDescription = null,
                tint = if (!isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                stringResource(R.string.settings_backup),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        },
        text = { Text(showBackupResultDialog!!) },
        confirmButton = {
            Button(onClick = {
                onDismissRequest()
            }) { Text(stringResource(R.string.ok)) }
        }
    )

}