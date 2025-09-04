# 📰 NewsEvents

**NewsEvents** is an Android application built with **Kotlin** and **Jetpack Compose** that delivers the latest news and events through a modern, paginated feed. It leverages the **Paging 3 library** and **Room database** for seamless offline caching, ensuring smooth scrolling and efficient data loading.

---

## 🚀 Features

* 📰 **News Feed** – Browse the latest articles with infinite scroll using **Paging 3**.
* 📂 **Offline Support** – Articles are cached locally with **Room Database** and **Remote Mediator**.
* 🔍 **Search Preferences** – Save and restore user search settings with **DataStore Preferences**.
* 🎨 **Modern UI** – Built entirely with **Jetpack Compose** and **Material 3**.
* 🌐 **API Integration** – Fetches live data from a remote **News API**.
* 🧩 **Dependency Injection** – Powered by **Hilt** for clean architecture and modularity.
* 📱 **Responsive Layouts** – Optimized for phones and tablets.

---

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose, Material 3
* **Architecture:** MVVM + Repository Pattern
* **Data:**

    * Retrofit (API calls)
    * Room + Paging 3 (local cache + pagination)
    * DataStore (user preferences)
* **Dependency Injection:** Hilt
* **Testing:** JUnit, Espresso
* **Build System:** Gradle (KTS DSL)

---

## 📂 Project Structure

```
NewsEvents/
│── app/
│   ├── src/main/
│   │   ├── java/com/example/newsevents/
│   │   │   ├── MainActivity.kt          # Entry point
│   │   │   ├── NewsEventsApp.kt         # Application class
│   │   │   ├── LocalHubNavHost.kt       # Navigation graph
│   │   │   ├── data/                    # Data layer (API, DB, Repository, Paging)
│   │   │   ├── di/                      # Hilt DI modules
│   │   │   ├── ui/feed/                 # Feed screen & ViewModel
│   │   └── res/                         # Resources (layouts, drawables, values)
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## ⚙️ Setup & Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/NewsEvents.git
   cd NewsEvents
   ```

2. **Open in Android Studio**

    * Use Android Studio Hedgehog or newer.
    * Ensure **Kotlin 1.9+** and **Compose Compiler Extension 1.5+** are installed.

3. **Build the project**

   ```bash
   ./gradlew build
   ```

4. **Run the app** on an emulator or connected device (Android 7.0+, API 24+).

---

## 🧪 Testing

Run unit and UI tests with:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

---