package com.codenytra.amme.el_8animah

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codenytra.amme.el_8animah.features.AnimatedAppIcon
import com.codenytra.amme.el_8animah.features.pinlock.PinLockActivity
import com.codenytra.amme.el_8animah.features.pinlock.PinManager
import com.codenytra.amme.el_8animah.features.settings.SettingsActivity
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme

class MainActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If a PIN is configured and this launch wasn't unlocked yet, redirect to PinLockActivity
        if (PinManager.hasPinSet(this) &&
            !intent.getBooleanExtra(PinLockActivity.EXTRA_PIN_VERIFIED, false)
        ) {
            startActivity(Intent(this, PinLockActivity::class.java))
            finish()
            return
        }

        // Enables modern edge-to-edge drawing under status and navigation bars
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                        )
                    )
                },
                actions = {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(context, SettingsActivity::class.java)
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(R.string.settings_title)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AnimatedAppIcon()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.app_desc),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    El8animahTheme {
        HomeScreen()
    }
}