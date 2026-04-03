# Data Layer & Business Logic Workflow 🏗️

This document provides a deep dive into the backend data flow of the Stocks App, detailing how the application manages state, interacts with remote APIs, and persists data locally.

---

## 1. High-Level Design (HLD)

The application adheres to the **MVVM (Model-View-ViewModel)** architectural pattern coupled with the **Repository Pattern**. This architecture ensures a clean separation of concerns:

*   **View (UI Layer)**: Activities and Fragments that observe `LiveData` from the ViewModel to render the UI.
*   **ViewModel (State Holder)**: Manages UI-related data, handles user interactions, and launches coroutines for data operations.
*   **Repository (Data Coordinator)**: Acts as the "Single Source of Truth." It orchestrates data flow between the network and the local database.
*   **Data Sources**:
    *   **Remote**: [Retrofit](https://square.github.io/retrofit/) for fetching real-time market data from the Alpha Vantage API.
    *   **Local**: [Room](https://developer.android.com/training/data-storage/room) for persistent storage of watchlists and offline caching.

---

## 2. Low-Level Design (LLD) & Component Breakdown

### A. The ViewModel (`StockViewModel.kt`)
*   **Connectivity Awareness**: Uses `hasInternetConnection()` to verify network availability before initiating remote requests, preventing unnecessary errors.
*   **State Wrapping**: Employs a `Resource<T>` wrapper (Success, Error, Loading) to communicate API states to the UI layer.
*   **Data Transformation**: Specifically in `fetchTimeSeries`, it maps complex nested API responses (like `IntradayResponse.kt`) into `Entry` objects compatible with the `MPAndroidChart` library.

### B. The Repository (`StockRepository.kt`)
The mediator between data sources.
*   **Network Operations**: Executes suspended functions such as `getTopGainersLosers()`, `getStockDetails(symbol)`, and `getIntraday(symbol)` via the API interface.
*   **Database Operations**: Manages local writes and reads through the DAO, ensuring data consistency.

### C. The API Layer (`StocksAPI.kt` & `RetrofitInstance.kt`)
*   Defines the REST endpoints for Alpha Vantage.
*   Uses `GsonConverterFactory` for automated JSON-to-POJO mapping into models like `StockDetailsResponse.kt`.

### D. The Database Layer (`StockDao.kt` & `StockDatabase.kt`)
*   **Reactive Queries**: Methods like `getWatchlists()` return `LiveData`, allowing the UI to react automatically to database changes.
*   **Relational Mapping**: Utilizes `WatchlistStockCrossRef` to implement a Many-to-Many relationship between Stocks and Watchlists.

---

## 3. Primary Write-Path: Adding a Stock to a Watchlist ✍️

This workflow demonstrates the seamless interconnection between the UI, Business Logic, and Persistence layers:

1.  **User Trigger**: The user selects "Add to Watchlist" from the `detailsFragment`.
2.  **ViewModel Entry**: `saveStockIntoWatchlists(stock, selectedWatchlists)` is called in `StockViewModel.kt`.
3.  **Step 1: Metadata Persistence**: The ViewModel calls `repository.insertStock(stock)`. This ensures the stock exists in the `Stock` table in `StockDatabase.kt`.
4.  **Step 2: Cross-Reference Mapping**: A loop iterates through each selected watchlist, calling `repository.insertWatchlistStockCrossRef(crossRef)`. This creates the logical link in the `WatchlistStockCrossRef` table in Room.
5.  **Automatic UI Update**: Since the `watchlistFragment` is observing `getWatchlists()` (which is a `LiveData` stream), Room triggers an automatic UI refresh as soon as the transaction completes. No manual "refresh" button is required.

---

## 4. Technical Mechanisms

*   **Concurrency**: All disk and network I/O are performed on background threads using **Kotlin Coroutines** (`viewModelScope`).
*   **Error Handling**: Centralized in `StockViewModel.kt` using `try-catch` blocks to handle `IOException` (network loss) and `HttpException` (API rate limits).
*   **Data Encapsulation**: Internal state is kept in `MutableLiveData` while the UI only accesses immutable `LiveData`.
*   **Relational Integrity**: Uses `@Transaction` in `StockDao.kt` for complex queries involving multiple tables to ensure atomic operations.