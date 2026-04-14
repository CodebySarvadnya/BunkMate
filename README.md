# 📚 BunkMate — Semester-Based Attendance Tracker

> A privacy-first Android app that helps university students track attendance across semesters — so you always know how many lectures you can actually afford to skip.

![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)
![Min SDK](https://img.shields.io/badge/Min%20SDK-API%2024-blue)
![License](https://img.shields.io/badge/License-MIT-orange)

---

## 📖 Table of Contents

- [The Problem & Solution](#-the-problem--solution)
- [Features](#-features)
- [Tech Stack & Architecture](#️-tech-stack--architecture)
- [Requirements](#️-requirements)
- [Project Setup](#-project-setup)
- [Generate Release APK](#-generate-release-apk)
- [Project Structure](#-project-structure)
- [Keystore Notice](#-keystore-notice)
- [Future Improvements](#-future-improvements)
- [Author](#-author)
- [License](#-license)

---

## 🎯 The Problem & Solution

Students often realize they are short on attendance only at the end of the term — when it's too late to fix it.

**BunkMate** solves this by:

- 📊 Showing a **live attendance dashboard** with your current percentage
- 🗂️ Keeping records **separated by semester** for a clean slate every term
- 📅 Maintaining a **date-wise history log** to review past decisions
- 📴 Working **fully offline** — no internet, no accounts needed

---

## ✨ Features

### 📅 Smart Semester Management
Create and switch between semesters freely. The database uses a **Composite Primary Key (Date + Semester)**, so the same date can exist across multiple semesters without any data conflicts.

### ➕ Quick Daily Entry
Log your attendance in seconds:
- Total lectures scheduled for the day
- Lectures actually attended
- Automatic daily percentage calculation

### 📜 Attendance History
A dedicated, filtered history screen per semester — giving you a chronological view of every entry to help you identify patterns over time.

### 🛡️ Privacy First — Fully Offline
All data is stored locally using **Room Database**. No login. No cloud sync. No tracking. Your attendance is your business.

### 🧮 Attendance Calculator
A standalone manual calculator — enter your total lectures and attended lectures to instantly compute your current attendance percentage, without affecting any saved records.

### 🎯 Target Attendance Planner
Set your desired attendance target (e.g. 75%), then enter your total lectures and attended lectures. The planner will:
- Tell you **how many lectures you can still bunk** if you are safely above your target.
- Tell you **how many consecutive lectures you must attend** if you have fallen below your target.

### 🔮 Future Attendance Prediction
Planning ahead? Enter your current lecture ratio along with how many upcoming lectures you intend to attend and how many you plan to skip — the app predicts what your attendance percentage will look like at the end of that period.

---

## 🛠️ Tech Stack & Architecture

Built using modern **Android Jetpack** components, following **MVVM / MVI** architecture principles.

| Layer | Technology |
|---|---|
| **Language** | Kotlin (Java 17 toolchain) |
| **UI** | Jetpack Compose (declarative, state-driven) |
| **Architecture** | MVVM / MVI principles |
| **Database** | Room + KSP (Kotlin Symbol Processing) |
| **Navigation** | Compose Navigation (Single Activity) |
| **Dependency Mgmt** | Gradle Version Catalog (TOML) |
| **Build System** | Kotlin DSL (`build.gradle.kts`) |

---

## ⚙️ Requirements

| Requirement | Version |
|---|---|
| **Android Studio** | Ladybug \| 2024.2.1 or newer |
| **Gradle Plugin** | 8.7+ |
| **Minimum SDK** | API 24 (Android 7.0 Nougat) |
| **Compile / Target SDK** | API 36 (Android 16) |

---

## 🧩 Project Setup

**1. Clone the repository**
```bash
git clone https://github.com/<your-username>/BunkMate.git
cd BunkMate
```

**2. Open in Android Studio**

Go to **File → Open**, select the cloned project folder, and allow Gradle to sync completely.

**3. Run the app**

Connect a physical device or start an emulator (API 24+), then click **Run ▶**.

---

## 📦 Generate Release APK

From Android Studio:
```
Build → Generate Signed Bundle / APK → APK → release
```

The signed APK will be output at:
```
app/build/outputs/apk/release/app-release.apk
```

You can rename and share this APK directly.

---

## 📁 Project Structure

```
BunkMate/
├── app/
│   ├── src/main/java/com/.../bunkmate/
│   │   ├── data/           → Room entities, DAO, Database
│   │   ├── ui/             → Jetpack Compose screens & components
│   │   ├── viewmodel/      → State management (MVVM/MVI)
│   │   └── MainActivity.kt
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml  → Centralized dependency version catalog
├── build.gradle.kts
└── settings.gradle.kts
```

---


## 👨‍💻 Author

Developed by **Sarvadnya Shingane**

If you found this project useful, please consider giving it a ⭐ — it helps a lot!
Feel free to fork, open issues, or contribute.

---

## 📄 License

```
MIT License

Copyright (c) 2025 Sarvadnya Shingane

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```

---

<div align="center">
  Made with ❤️ for students who bunk — responsibly.
</div>
