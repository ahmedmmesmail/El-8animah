package com.codenytra.amme.el_8animah.features.backup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.codenytra.amme.el_8animah.R

@Composable
fun ResetDialog(
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},

    ) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = { Icon(Icons.Rounded.Warning, null, tint = MaterialTheme.colorScheme.error) },
        title = {
            Text(
                stringResource(R.string.settings_reset),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        },
        text = { Text(stringResource(R.string.settings_reset_confirm)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text(stringResource(R.string.settings_reset_confirm_yes)) }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.settings_reset_confirm_no))
            }
        }
    )
}