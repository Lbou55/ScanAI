Here's your complete `README.md` :

```markdown
# ScanAI 📷🤖

An Android application that scans and identifies objects using the device camera and Google Gemini AI. Each scan is saved locally and accessible in a history screen.

---

## Features

- Real-time camera preview via CameraX
- AI object identification in French using Google Gemini 2.5 Flash
- Local storage of scan history with Room / SQLite
- Confidence bar (green / orange / red)
- Automatic retry on API rate limit (429)
- MVVM architecture with Jetpack Compose

---

## Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Main language |
| Jetpack Compose | UI Framework |
| CameraX | Camera preview and capture |
| Room / SQLite | Local database |
| Retrofit + OkHttp | HTTP client for Gemini API |
| Google Gemini API | AI object classification |
| Navigation Compose | Screen navigation |
| MVVM | Architecture pattern |

---

## Project Structure

```
com.example.scanai/
├── MainActivity.kt
├── ui/screens/
│   ├── CameraScreen.kt
│   └── HistoryScreen.kt
├── viewmodel/
│   ├── CameraViewModel.kt
│   └── HistoryViewModel.kt
└── data/
    ├── local/          → Room (Entity, Dao, Database)
    ├── model/          → UiState, ClassificationResult
    ├── remote/         → GeminiService, ApiClient, ApiKeys
    └── repository/     → ScanRepository
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android device or emulator with API 26+
- A Google account to get a Gemini API key

---

## How to Get the Gemini API Key

1. Go to **[aistudio.google.com](https://aistudio.google.com)**
2. Sign in with your Google account
3. Click **Get API Key** in the top left
4. Click **Create API Key**
5. Copy the generated key (starts with `AIza...`)

---

## Configuration

1. Clone the repository
```bash
git clone https://github.com/Lbou55/ScanAI.git
```

2. Open the project in Android Studio

3. Create the file `ApiKeys.kt` in this path :
```
app/src/main/java/com/example/scanai/data/remote/ApiKeys.kt
```

4. Add your Gemini API key :
```kotlin
package com.example.scanai.data.remote

object ApiKeys {
    const val GEMINI_API_KEY = "YOUR_GEMINI_API_KEY_HERE"
}
```

5. Sync Gradle and run the app

---

## How to Use

1. Launch the app and grant camera permission
2. Point the camera at any object
3. Tap the **Scanner** button
4. Wait a few seconds for Gemini to analyze the image
5. The detected object name is displayed in French
6. All scans are saved and visible in the **Historique** tab

---

## Important Notes

- `ApiKeys.kt` is listed in `.gitignore` and will never be pushed to GitHub
- The free tier of Gemini allows 15 requests per minute
- If you get a 429 error, the app will automatically retry up to 3 times
