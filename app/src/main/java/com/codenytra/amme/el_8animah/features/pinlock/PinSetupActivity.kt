package com.codenytra.amme.el_8animah.features.pinlock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codenytra.amme.el_8animah.BaseActivity
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme

/*
The Four Steps

VERIFY        → Confirm the old PIN (if a PIN exists)
ENTER         → Enter the new PIN
CONFIRM       → Confirm the new PIN
BIOMETRIC_OPT → Choose to enable fingerprint authentication (if the device supports it)
*/
private enum class SetupStep { VERIFY, ENTER, CONFIRM, BIOMETRIC_OPT }

@OptIn(ExperimentalMaterial3Api::class)
class PinSetupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            El8animahTheme {
                PinSetupScreen(
                    onDone = { finish() },
                    onCancel = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinSetupScreen(onDone: () -> Unit, onCancel: () -> Unit) {

    val context = LocalContext.current

    val hasExistingPin = remember { PinManager.hasPinSet(context) }

    val deviceSupportsBiometric = remember {
        BiometricManager.from(context)
            .canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }

    var step by remember { mutableStateOf(if (hasExistingPin) SetupStep.VERIFY else SetupStep.ENTER) }
    var currentInput by remember { mutableStateOf("") }
    var newPinTemp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<Int?>(null) }

    fun afterPinSaved() {
        if (deviceSupportsBiometric) {
            step = SetupStep.BIOMETRIC_OPT
        } else {
            onDone()
        }
    }

    fun onDigit(digit: String) {
        if (currentInput.length >= 4) return
        currentInput += digit
        errorMessage = null

        if (currentInput.length == 4) {
            when (step) {
                SetupStep.VERIFY -> {
                    if (PinManager.verifyPin(context, currentInput)) {
                        step = SetupStep.ENTER
                        currentInput = ""
                    } else {
                        errorMessage = R.string.pin_error_incorrect
                        currentInput = ""
                    }
                }

                SetupStep.ENTER -> {
                    newPinTemp = currentInput
                    step = SetupStep.CONFIRM
                    currentInput = ""
                }

                SetupStep.CONFIRM -> {
                    if (currentInput == newPinTemp) {
                        PinManager.setPin(context, currentInput)
                        afterPinSaved()
                    } else {
                        errorMessage = R.string.pin_error_mismatch
                        newPinTemp = ""
                        step = SetupStep.ENTER
                        currentInput = ""
                    }
                }
//              BIOMETRIC_OPT doesn't need a keypad — don't handle it here.
                SetupStep.BIOMETRIC_OPT -> {}
            }
        }
    }

    fun onDelete() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            errorMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (hasExistingPin) stringResource(R.string.change_pin)
                        else stringResource(R.string.set_pin),
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                },
                colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->

        // ── If BIOMETRIC_OPT step → Selection screen ────────
        // Otherwise → Standard screen with keypad
        if (step == SetupStep.BIOMETRIC_OPT) {
            BiometricOptInScreen(
                modifier = Modifier.padding(innerPadding),
                onEnable = {
                    // User agreed → we save 'true' and we're done.
                    PinManager.setBiometricEnabled(context, true)
                    onDone()
                },
                onSkip = {
                    // User refused → save 'false' and finish.
                    PinManager.setBiometricEnabled(context, false)
                    onDone()
                }
            )
        } else {
        // ── Standard PIN screen ───────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = when (step) {
                        SetupStep.VERIFY -> stringResource(R.string.pin_step_verify)
                        SetupStep.ENTER -> stringResource(R.string.pin_step_enter)
                        SetupStep.CONFIRM -> stringResource(R.string.pin_step_confirm)
                        else -> ""
                    },
                    style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )

                PinDotsIndicator(filledCount = currentInput.length)

                Box(modifier = Modifier.height(20.dp)) {
                    if (errorMessage != null) {
                        Text(
                            text = stringResource(errorMessage!!),
                            color = MaterialTheme.colorScheme.error,
                            style = TextStyle(fontSize = 13.sp, textAlign = TextAlign.Center)
                        )
                    }
                }

                PinKeypad(onDigit = ::onDigit, onDelete = ::onDelete)
            }
        }
    }
}

@Composable
private fun BiometricOptInScreen(
    modifier: Modifier = Modifier,
    onEnable: () -> Unit,
    onSkip: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.Fingerprint,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.biometric_setup_title),
                style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = stringResource(R.string.biometric_setup_description),
                style = TextStyle(
                    fontSize = 14.sp, textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onEnable,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Icon(Icons.Rounded.Fingerprint, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.biometric_setup_enable), fontSize = 15.sp)
        }

        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Text(stringResource(R.string.biometric_setup_skip), fontSize = 15.sp)
        }
    }
}

@Composable
fun PinDotsIndicator(filledCount: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isFilled = index < filledCount
            val color by animateColorAsState(
                targetValue = if (isFilled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                animationSpec = tween(durationMillis = 150),
                label = "dot_color_$index"
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// ============================================================
// PIN KEYPAD — نفس ما كان
// ============================================================
@Composable
fun PinKeypad(onDigit: (String) -> Unit, onDelete: () -> Unit) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "DEL")
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        when (key) {
                            "" -> {}
                            "DEL" -> {
                                IconButton(onClick = onDelete, modifier = Modifier.size(72.dp)) {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.Backspace,
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            else -> {
                                FilledTonalButton(
                                    onClick = { onDigit(key) },
                                    modifier = Modifier.size(72.dp),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = key,
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
