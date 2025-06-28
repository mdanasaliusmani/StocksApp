package com.SDE.stocksapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.models.StockWithWatchlist
import com.SDE.stocksapp.models.Watchlist
import com.SDE.stocksapp.models.WatchlistStockCrossRef
import com.SDE.stocksapp.models.WatchlistWithStock

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stock): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: Watchlist): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistStockCrossRef(watchlistStockCrossRef: WatchlistStockCrossRef)

    @Transaction
    @Query("SELECT * FROM Stock WHERE ticker = :ticker")
    fun getWatchlistsForStock(ticker: String): LiveData<List<StockWithWatchlist>>

    @Transaction
    @Query("SELECT * FROM Watchlist WHERE watchlistName = :watchlistName")
    fun getStocksForWatchlist(watchlistName: String): LiveData<List<WatchlistWithStock>>

    @Transaction
    @Query("SELECT * FROM Watchlist")
    fun getWatchlists(): LiveData<List<Watchlist>>

    @Delete
    suspend fun deleteStock(stock: Stock)

    @Delete
    suspend fun deleteWatchlist(watchlist: Watchlist)

    @Transaction
    @Query("DELETE FROM WatchlistStockCrossRef WHERE watchlistName = :watchlistName")
    suspend fun deleteWatchlistStocks(watchlistName: String)

    @Transaction
    @Query("DELETE FROM WatchlistStockCrossRef WHERE ticker = :ticker")
    suspend fun deleteStockWatchlists(ticker: String)

    @Transaction
    @Query("DELETE FROM WatchlistStockCrossRef WHERE ticker = :ticker AND watchlistName = :watchlistName")
    suspend fun deleteStockFromWatchlist(ticker: String, watchlistName: String)

}