# PIN Lock & Biometric Security Feature

A robust, enterprise-grade security layer for Android applications. It provides PIN code protection (with SHA-256 cryptographic hashing) alongside seamless **Biometric Authentication** (Fingerprint and Face Unlock) via AndroidX Biometric.

---

## 🌟 Key Features

- 🔒 **Secure PIN Management**: Protects user access with a 4-digit PIN stored securely using **SHA-256** hashing.
- 👆 **Biometric Prompt (Fingerprint & Face)**: Integrates `androidx.biometric:biometric` for instant biometric unlocking.
- 🎨 **Modern Compose Numeric Keypad**: Custom-styled interactive numeric pad with haptic feedback, clear buttons, and animated dot indicators.
- 🛡️ **Two-Step Setup & Verification**: Includes a guided setup flow (Create PIN -> Confirm PIN -> Biometric prompt).
- 🔄 **Activity Interceptor**: Easily gate the app launcher or sensitive activities behind the lock screen.

---

## 📁 File Structure

```text
features/pinlock/
├── PinLockActivity.kt    # Lock screen interface shown on app entry / locked state
├── PinManager.kt         # Secure SHA-256 PIN hashing, verification & SharedPreferences storage
└── PinSetupActivity.kt   # Wizard screen for creating, changing, and confirming PINs
```

---

## 🚀 How to Implement in Your Own Project

### 1. Add Dependencies & Permissions

In your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
}
```

In your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />

<activity
    android:name=".features.pinlock.PinLockActivity"
    android:exported="false"
    android:theme="@style/Theme.El8animah" />

<activity
    android:name=".features.pinlock.PinSetupActivity"
    android:exported="false"
    android:theme="@style/Theme.El8animah" />
```

---

### 2. Copy `PinManager.kt`

`PinManager` handles hash calculation and state:

```kotlin
object PinManager {
    fun hasPinSet(context: Context): Boolean = ...
    fun setPin(context: Context, pin: String) = ...
    fun verifyPin(context: Context, pin: String): Boolean = ...
    fun clearPin(context: Context) = ...
    fun isBiometricEnabled(context: Context): Boolean = ...
    fun setBiometricEnabled(context: Context, enabled: Boolean) = ...
}
```

---

### 3. Protect App Entry (`MainActivity.kt` or `BaseActivity.kt`)

In your launcher Activity `onCreate()`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (PinManager.hasPinSet(this)) {
        val intent = Intent(this, PinLockActivity::class.java)
        startActivity(intent)
        // Finish or wait for result depending on your navigation flow
    }
}
```

---

### 4. Trigger PIN Setup from Settings

To let users create or change their PIN:

```kotlin
val intent = Intent(context, PinSetupActivity::class.java)
context.startActivity(intent)
```

---

### 5. Launching Biometric Authentication (`PinLockActivity.kt`)

```kotlin
fun showBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)
    val prompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(activity.getString(R.string.use_biometric))
        .setSubtitle(activity.getString(R.string.biometric_subtitle))
        .setNegativeButtonText(activity.getString(R.string.use_pin_instead))
        .build()

    prompt.authenticate(promptInfo)
}
```
