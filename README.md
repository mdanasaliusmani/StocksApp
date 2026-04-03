# 📈 Stocks App

A professional, high-performance Android application for tracking stocks and ETFs in real-time. Built with a focus on modern architectural patterns and reactive programming, this app provides a seamless experience for monitoring market trends and managing personalized watchlists.

## 🚀 Project Overview

**Stocks App** is a native Android solution designed for investors who need reliable market data at their fingertips. It leverages the **Alpha Vantage API** to deliver real-time insights into market movers and provides interactive visualizations for stock performance history. The app is architected with scalability and offline-first principles in mind.

## ✨ Key Features

*   **Market Pulse:** Real-time tracking of Top Gainers, Top Losers, and Most Active stocks.
*   **Custom Watchlists:** Create and manage multiple watchlists with local persistence.
*   **Interactive Line Graphs:** High-fidelity price trend analysis across Intraday, Daily, and Weekly timeframes.
*   **Detailed Insights:** Comprehensive financial metrics including Market Cap, P/E Ratio, and Dividend Yield.
*   **Offline-First Experience:** Robust caching of market data and watchlists using Room.
*   **Modern Material UI:** Clean Material 3 design with smooth transitions, Jetpack Navigation and Lottie Animations.

## 🛠 Tech Stack

*   **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern.
*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **Networking:** [Retrofit](https://square.github.io/retrofit/) & OkHttp.
*   **Local Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room).
*   **Navigation:** [Jetpack Navigation Component](https://developer.android.com/guide/navigation).
*   **UI Framework:** Material Design 3, ViewBinding, and DataBinding.
*   **Charts:** [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart).
*   **Image Loading:** [Glide](https://github.com/bumptech/glide).
*   **Concurrency:** Kotlin Coroutines & Flow.

## 📖 Documentation & Guides

For a deeper dive into how the app works and how to contribute, check out the following guides:

*   **[Contributing Guidelines](CONTRIBUTING.md)** - Start here if you want to help build this project.
*   **[Data Layer & Business Logic Workflow](docs/data_layer_workflow.md)** - Deep dive into repositories, APIs, and threading.
*   **[UI & Navigation Workflow](docs/ui_navigation_workflow.md)** - Understanding the Single Activity structure and NavGraph.
*   **[Database Schema & Relations](docs/database_schema.md)** - Details on Room entities and Many-to-Many relations.

## 🏁 Getting Started

### 1. Prerequisites
*   Android Studio Ladybug+
*   JDK 11+
*   An [Alpha Vantage API Key](https://www.alphavantage.co/support/#api-key).

### 2. Configuration
The app requires an API key to fetch live data.

1.  Open `app/src/main/java/com/SDE/stocksapp/util/Constants.kt`.
2.  Update the `API_KEY` constant:

```kotlin
// File: app/src/main/java/com/SDE/stocksapp/util/Constants.kt
const val API_KEY = "YOUR_ALPHA_VANTAGE_KEY_HERE"
```

> **Note:** The project currently uses `com.example.newsapp.util` as the package for utility classes. Ensure your imports match this structure.

### 3. Build and Run
*   Sync project with Gradle.
*   Run on an emulator or physical device.

## 📂 Project Structure

*   `api/`: Retrofit service definitions and API configuration.
*   `db/`: Room database, DAOs, and persistence logic.
*   `models/`: Data classes for API responses and database entities.
*   `repository/`: Data layer abstraction and coordination.
*   `ui/`: Fragments, ViewModels, and UI state management.
*   `adapters/`: RecyclerView adapters for various list displays.

---
*Developed with modern Android standards.*
