# Database Schema and Relations 🗄️

The Stocks App uses **Room Persistence Library** to manage a local SQLite database. This document details the entities and the relationships that enable features like custom watchlists and offline caching.

---

## 1. Entity Definitions

### `Stock` (`Stock.kt`)
Represents a financial instrument.
*   **Primary Key**: `ticker` (e.g., "AAPL").
*   **Fields**: `price`, `change_amount`, `change_percentage`, and `volume`.
*   **Purpose**: Acts as the central registry for all stocks that have been viewed or added to a watchlist.

### `Watchlist` (`Watchlist.kt`)
Represents a user-created collection of stocks.
*   **Primary Key**: `watchlistName` (e.g., "Tech Favorites").
*   **Purpose**: Allows users to group stocks logically.

### `WatchlistStockCrossRef` (`WatchlistStockCrossRef.kt`)
A junction table used to facilitate the Many-to-Many relationship.
*   **Primary Keys**: Composite key of `(watchlistName, ticker)`.
*   **Purpose**: Maps which stocks belong to which watchlists. A single stock can exist in multiple watchlists, and a single watchlist can contain many stocks.

---

## 2. Many-to-Many Relationship Architecture

To implement the relationship where **Stocks** and **Watchlists** are interconnected, we use the `WatchlistWithStock` and `StockWithWatchlist` data classes.

### How it works:
1.  Room identifies the `WatchlistStockCrossRef` as the bridge.
2.  Using the `@Relation` annotation with a `@Junction`, Room automatically handles the complex JOIN queries required to fetch related entities.

```kotlin
// Example from WatchlistWithStock.kt
data class WatchlistWithStock(
    @Embedded val watchlist: Watchlist,
    @Relation(
        parentColumn = "watchlistName",
        entityColumn = "ticker",
        associateBy = Junction(WatchlistStockCrossRef::class)
    )
    val stocks: List<Stock>
)
```

---

## 3. Querying Relations in `StockDao.kt`

The `StockDao` interface defines how the app interacts with these relationships. We use `@Transaction` to ensure that the multiple queries required to build the relational objects are executed atomically.

### Example: Fetching Stocks for a Watchlist
When the user opens a specific watchlist, the app calls `getStocksForWatchlist`.

```kotlin
@Transaction
@Query("SELECT * FROM Watchlist WHERE watchlistName = :watchlistName")
fun getStocksForWatchlist(watchlistName: String): LiveData<List<WatchlistWithStock>>
```

**What happens under the hood:**
1.  Room first queries the `Watchlist` table for the specific name.
2.  It then queries the `WatchlistStockCrossRef` to find all `ticker` symbols associated with that name.
3.  Finally, it fetches the full `Stock` details for each ticker found.
4.  The results are emitted as a `LiveData` stream, ensuring the UI stays updated if the watchlist content changes.

---

## 4. Relational Integrity & Cleanup

The `StockDao` also manages the lifecycle of these relationships:
*   **`deleteWatchlistStocks(watchlistName)`**: Removes all associations for a watchlist when it is deleted.
*   **`deleteStockFromWatchlist(ticker, watchlistName)`**: Surgical removal of a single stock from a specific list without affecting its presence in other lists or the main `Stock` table.