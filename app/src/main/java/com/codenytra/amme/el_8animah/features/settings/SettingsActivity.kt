package com.codenytra.amme.el_8animah.features.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codenytra.amme.el_8animah.features.settings.ui.theme.El8animahTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            SettingsSection(
                header = "General",
                content = listOf(
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = true,
                        isBottom = false,
                        text = "Appearance",
                        description = "Theme",
                        icon = Icons.Rounded.Palette
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = true,
                        text = "Language",
                        description = "",
                        icon = Icons.Rounded.Language
                    ),

                )
            )
            SettingsSection(
                header = "About & Support",
                content = listOf(
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = true,
                        isBottom = false,
                        text = "Privacy policy",
                        description = "",
                        icon = Icons.Rounded.Policy
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = false,
                        text = "Rate App",
                        description = "",
                        icon = Icons.Rounded.StarRate
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = false,
                        text = "Share App",
                        description = "",
                        icon = Icons.Rounded.Share
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = false,
                        text = "Send Feedback",
                        description = "",
                        icon = Icons.Rounded.Feedback
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = true,
                        text = "Our Apps",
                        description = "",
                        icon = Icons.Rounded.Apps
                    ),
                    ModernButton(
                        onClick = { /*TODO*/ },
                        isTop = false,
                        isBottom = true,
                        text = "About",
                        description = "",
                        icon = Icons.Rounded.Person
                    ),
                )
            )
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

@Composable
fun ModernButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isTop: Boolean = false,
    isBottom: Boolean = false,
    text: String,
    description: String,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(
                top = if (isTop) 16.dp else 8.dp,
                bottom = if (isBottom) 16.dp else 8.dp,
                start = 8.dp,
                end = 8.dp
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = if (isTop) 16.dp else 4.dp,
            topEnd = if (isTop) 16.dp else 4.dp,
            bottomStart = if (isBottom) 16.dp else 4.dp,
            bottomEnd = if (isBottom) 16.dp else 4.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text
            )
        }
        Column(
            modifier = Modifier.padding(start = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun Header(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

data class ModernButton (
    val onClick: () -> Unit,
    val isTop: Boolean,
    val isBottom: Boolean,
    val text: String,
    val description: String,
    val icon: ImageVector
)
@Composable
fun SettingsSection(
    header: String,
    content: List<ModernButton>
) {
    Header(header)

    LazyColumn(

    ) {
        content.forEach { button ->
            item {
                ModernButton(
                    onClick = button.onClick,
                    isTop = button.isTop,
                    isBottom = button.isBottom,
                    text = button.text,
                    description = button.description,
                    icon = button.icon
                )
            }
        }
    }
}