# El-8animah (الغنيمة) Architecture & Features Cookbook

Welcome to the **El-8animah** features directory. This repository is designed as a reusable cookbook and reference architecture for Android developers building modern Jetpack Compose applications.

---

## 📂 Features Directory Index

| Feature | Description | Documentation |
| :--- | :--- | :--- |
| 🎨 **Appearance** | Dynamic theming (Material You), Light/Dark/System modes, Custom seed palettes, and OLED Pure Black. | [Appearance README](appearance/README.md) |
| 📦 **Backup & Restore** | Complete app data archiving (`shared_prefs` & SQLite/Room DBs) to `.8animah` zip with hot restart via `AlarmManager`. | [Backup README](backup/README.md) |
| 💬 **Feedback** | Material 3 ModalBottomSheet with categorization chips and `ACTION_SENDTO` email intents. | [Feedback README](feecback/README.md) |
| 🌍 **Localization** | Dynamic runtime language switching across 12+ languages + System Default with automatic RTL/LTR handling. | [Localization README](localization/README.md) |
| 🔒 **PIN Lock & Biometrics** | Security layer featuring SHA-256 hashed PIN verification, custom keypad, and AndroidX BiometricPrompt (Fingerprint/Face). | [PIN Lock README](pinlock/README.md) |
| ⚙️ **Settings Dashboard** | Scalable, card-based modular Settings screen architecture in Jetpack Compose. | [Settings README](settings/README.md) |
| 👨‍💻 **About Developer** | Developer profile showcase with social links, portfolio intent launcher, and animated headers. | `AboutActivity.kt` |
| 🚀 **Our Apps Showcase** | Portfolio screen highlighting developer apps with Google Play direct deep-linking. | `OurAppsActivity.kt` |

---

## 🛠️ Technology Stack
- **Language**: Kotlin 2.0+
- **UI Toolkit**: Jetpack Compose & Material Design 3 (M3)
- **Architecture**: Clean Architecture / MVVM with StateFlow & Coroutines
- **Target SDK**: Android 14 / Android 15 (API 34/35) — Minimum SDK: Android 8.0 (API 26)
