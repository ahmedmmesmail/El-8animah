package com.codenytra.amme.el_8animah.features.pinlock

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.codenytra.amme.el_8animah.BaseActivity
import com.codenytra.amme.el_8animah.MainActivity
import com.codenytra.amme.el_8animah.R
import com.codenytra.amme.el_8animah.ui.theme.El8animahTheme


class PinLockActivity : BaseActivity() {

    // ── State مشترك بين الـ Activity والـ Composable ────────
    private val _canUseBiometric = mutableStateOf(false)

    // ── BiometricPrompt — بنبنيها هنا في الـ Activity ───────
    // "this" هنا = FragmentActivity = صح 100%
    // مش محتاجين cast ولا LocalContext
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ── نشيك البصمة: شرطين لازم يتحققوا ────────────
        // 1. الجهاز يدعم البصمة (hardware + مسجلة)
        // 2. اليوزر وافق على تفعيلها في PinSetupActivity
        val deviceSupportsBiometric = BiometricManager.from(this)
            .canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
        val userEnabledBiometric = PinManager.isBiometricEnabled(this)
        _canUseBiometric.value = deviceSupportsBiometric && userEnabledBiometric

        // ── بناء BiometricPrompt ──────────────────────────
        // "this" هنا = FragmentActivity مباشرة — لا cast لا مشاكل
        biometricPrompt = BiometricPrompt(
            this,
            ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    navigateToApp()
                }

                override fun onAuthenticationFailed() {
                    // البصمة غلط — الـ BiometricPrompt بيتعامل مع الـ retries تلقائياً
                }

                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    // اليوزر ضغط "Use PIN" أو ألغى — نسيبه يكتب PIN يدوياً
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.use_pin_instead))
            .setAllowedAuthenticators(BIOMETRIC_WEAK)
            .build()

        setContent {
            El8animahTheme {
                PinLockScreen(
                    canUseBiometric  = _canUseBiometric.value,
                    onBiometricClick = { biometricPrompt.authenticate(promptInfo) },
                    onUnlocked       = { navigateToApp() }
                )
            }
        }

        // نشغّل البصمة تلقائياً عند فتح الشاشة
        if (_canUseBiometric.value) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun navigateToApp() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // بنبعت extra عشان MainActivity تعرف إن الـ PIN اتتحقق منه
                // ومش تشيك تاني وتفتح PinLockActivity في لوب لا نهائي
                putExtra(EXTRA_PIN_VERIFIED, true)
            }
        )
    }

    companion object {
        // اسم الـ extra — const عشان مش نغلط في الكتابة
        const val EXTRA_PIN_VERIFIED = "pin_verified"
    }
}

// ============================================================
// PIN LOCK SCREEN
//
// الـ Composable بقت "dumb" — مش عندها BiometricPrompt
// بس بتعرض الـ UI وبتنادي callbacks جاية من الـ Activity
//
// Parameters:
// - canUseBiometric: هل نظهر زر البصمة؟
// - onBiometricClick: لما اليوزر يضغط زر البصمة
// - onUnlocked: لما الـ PIN يكون صح
// ============================================================
@Composable
fun PinLockScreen(
    canUseBiometric:  Boolean,
    onBiometricClick: () -> Unit,
    onUnlocked:       () -> Unit
) {
    val context = LocalContext.current

    var currentInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    @SuppressLint("LocalContextGetResourceValueCall")
    fun onDigit(digit: String) {
        if (currentInput.length >= 4) return
        currentInput += digit
        errorMessage = null

        if (currentInput.length == 4) {
            if (PinManager.verifyPin(context, currentInput)) {
                onUnlocked()
            } else {
                errorMessage = context.getString(R.string.incorrect_pin)
                currentInput = ""
            }
        }
    }

    fun onDelete() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            errorMessage = null
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color    = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier            = Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text  = stringResource(R.string.app_name),
                    style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.enter_pin),
                    style = TextStyle(fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center)
                )
            }

            PinDotsIndicator(filledCount = currentInput.length)

            Box(modifier = Modifier.height(20.dp)) {
                if (errorMessage != null) {
                    Text(
                        text  = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = TextStyle(fontSize = 13.sp, textAlign = TextAlign.Center)
                    )
                }
            }

            PinKeypad(onDigit = ::onDigit, onDelete = ::onDelete)

            if (canUseBiometric) {
                TextButton(onClick = onBiometricClick) {
                    Icon(Icons.Rounded.Fingerprint, contentDescription = null,
                        modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.use_biometric),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
