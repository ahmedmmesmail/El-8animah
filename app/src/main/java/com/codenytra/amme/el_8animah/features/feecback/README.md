# In-App Feedback Feature

A user-friendly, interactive Material 3 BottomSheet component that allows users to send feedback, bug reports, and feature suggestions directly to the developer via their preferred email client.

---

## 🌟 Key Features

- 📱 **Interactive Modal BottomSheet**: Built with Jetpack Compose Material 3 (`ModalBottomSheet`).
- 🏷️ **Type Categorization**: Filter chips for **Bug Report**, **Feature Request**, and **Other**.
- ✉️ **Direct Email Intent**: Uses `Intent.ACTION_SENDTO` with the `mailto:` scheme to trigger email apps exclusively without showing irrelevant sharing targets.
- 🌐 **Localized**: Integrated with string resources across all supported application languages.

---

## 📁 File Structure

```text
features/feecback/
└── FeedbackSheet.kt   # ModalBottomSheet dialog with category chips, text field & email intent
```

---

## 🚀 How to Implement in Your Own Project

### 1. Add Dependencies

Ensure you have Material 3 and Compose dependencies:

```kotlin
dependencies {
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
}
```

---

### 2. Configure Strings (`res/values/strings.xml`)

Add the required string resources to your `strings.xml`:

```xml
<string name="feedback_title">Send Feedback</string>
<string name="feedback_type_label">Type</string>
<string name="feedback_type_bug">Bug Report</string>
<string name="feedback_type_feature">Feature Request</string>
<string name="feedback_type_other">Other</string>
<string name="feedback_message_hint">Describe your feedback here…</string>
<string name="feedback_send">Send</string>
<string name="feedback_cancel">Cancel</string>
```

---

### 3. Copy `FeedbackSheet.kt` and Customize Email

Update the recipient email address in `FeedbackDialog`:

```kotlin
val email = "mailto:your-email@example.com"
```

---

### 4. Integrate into your Compose UI

In any Screen or Activity (e.g. `SettingsActivity.kt`):

```kotlin
var showFeedbackSheet by remember { mutableStateOf(false) }

// Trigger button/row
SettingsRow(
    title = stringResource(R.string.settings_feedback),
    subtitle = stringResource(R.string.settings_feedback_desc),
    icon = Icons.Rounded.Feedback,
    onClick = { showFeedbackSheet = true }
)

// Show BottomSheet
if (showFeedbackSheet) {
    FeedbackDialog(
        onDismiss = { showFeedbackSheet = false }
    )
}
```

---

## 💡 Pro-Tip: Why `ACTION_SENDTO` with `mailto:`?

Using `Intent.ACTION_SEND` with MIME type `text/plain` triggers an overwhelming chooser with WhatsApp, Bluetooth, Twitter, etc.

Using `Intent(Intent.ACTION_SENDTO, "mailto:...".toUri())` ensures that **only dedicated email applications** (Gmail, Outlook, ProtonMail, etc.) are opened!
