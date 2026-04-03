# UI and Navigation Workflow 🧭

This document outlines the architectural structure of the user interface and the navigation patterns implemented in the **Stocks App**.

---

## 🏗 High-Level UI Structure

The application follows the **Single Activity Architecture** using the Jetpack Navigation Component.

*   **Host Activity (`StocksActivity.kt`)**: The main entry point that hosts the `NavHostFragment` and the `BottomNavigationView`.
*   **Navigation Host**: The `stocks_nav_graph.xml` defines all possible screens (fragments) and the paths between them.
*   **Bottom Navigation**: Integrated directly with the `NavController` to allow seamless switching between the **Home** and **Watchlist** top-level destinations.

---

## 🗺 Navigation Paths & Actions

### 1. Home Screen (`homeFragment`)
The Home screen serves as the central hub for market discovery.
*   **To Top Gainers/Losers**: Users can click "View All" on the Gainers or Losers sections.
    *   *Action*: `action_homeFragment_to_topGainersFragment`
    *   *Action*: `action_homeFragment_to_topLosersFragment`
*   **To Stock Details**: Clicking any individual stock card in the horizontal lists.
    *   *Action*: `action_homeFragment_to_detailsFragment`
    *   *Safe Args*: Passes a `Stock` object (Parcelable/Serializable) containing the ticker and current price.

### 2. Watchlist Screen (`watchlistFragment`)
Manages user-defined groups of stocks.
*   **To Watchlist Content**: Clicking on a specific watchlist name.
    *   *Action*: `action_watchlistFragment_to_watchlistStocksFragment`
    *   *Safe Args*: Passes `watchlistName` (String).
*   **To Stock Details**: Direct navigation from a watchlist item.
    *   *Action*: `action_watchlistFragment_to_detailsFragment`

### 3. Stock Details (`detailsFragment`)
The destination for in-depth analysis. It receives the `Stock` object and uses the ticker to fetch:
*   Real-time Intraday data.
*   Daily/Weekly historical trends for the `MPAndroidChart`.
*   Company Overview (Market Cap, P/E Ratio, etc.).

---

## 🖱 Event Handling in Adapters

The app uses specialized adapters to bridge data and user interaction:

### GenericStockAdapter
Used in `homeFragment`, `topGainersFragment`, and `topLosersFragment`.
*   **Pattern**: Exposes an `onItemClickListener` lambda.
*   **Trigger**: When a user taps the `root` layout of a stock item, the lambda is invoked with the specific `Stock` model.
*   **Navigation**: The Fragment listening to this event uses `findNavController().navigate()` with the appropriate Action and the `Stock` bundle.

### WatchlistAdapter
Used in the `watchlistFragment` to display different user watchlists.
*   **Trigger**: Clicking a watchlist item navigates the user to `watchlistStocksFragment`, passing the name of the list to filter the database query.

---

## 💡 Important implementation Details

*   **Safe Args**: The project uses the Safe Args plugin to ensure type-safety when passing data between fragments. The `Stock` model implements `Serializable` to be passed via NavBundles.
*   **Animations**: All transitions between fragments use custom animations defined in `res/anim/` (e.g., `slide_in_right`, `slide_out_left`) for a fluid "push-pop" feel.
*   **Edge-to-Edge**: `StocksActivity` utilizes `enableEdgeToEdge()` to provide an immersive UI experience that extends behind the system bars.
*   **ViewModel Sharing**: Fragments obtain the `StockViewModel` via `(activity as StocksActivity).viewModel`, allowing them to share the same repository instance and data state.
