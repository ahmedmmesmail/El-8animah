package com.codenytra.amme.el_8animah.features.feecback

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.codenytra.amme.el_8animah.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun FeedbackDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var selectedType by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    val email = "mailto:ahmedmme.26@gmail.com"

    val types = listOf(
        R.string.feedback_type_bug,
        R.string.feedback_type_feature,
        R.string.feedback_type_other
    )
    val typeIcons =
        listOf(Icons.Rounded.BugReport, Icons.Rounded.Lightbulb, Icons.Rounded.ChatBubble)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.feedback_title),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.feedback_type_label).uppercase(),
                    style = TextStyle(
                        fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp, color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    types.forEachIndexed { i, typeRes ->
                        item {
                            FilterChip(
                                selected = selectedType == i,
                                onClick = { selectedType = i },
                                label = { Text(stringResource(typeRes), fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(
                                        typeIcons[i],
                                        null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                shape = MaterialTheme.shapes.small
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text(stringResource(R.string.feedback_message_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = 5
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large
                    ) { Text(stringResource(R.string.feedback_cancel)) }

                    Button(
                        onClick = {
                            val feedbackType = context.getString(types[selectedType])
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = email.toUri()
                                putExtra(Intent.EXTRA_SUBJECT,
                                    "[$feedbackType] ${context.getString(R.string.app_name)} Feedback")
                                putExtra(Intent.EXTRA_TEXT, message)
                            }
                            context.startActivity(emailIntent)
                            onDismiss()
                        },
                        enabled = message.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large
                    ) { Text(stringResource(R.string.feedback_send)) }
                }
            }
        }
    }
}