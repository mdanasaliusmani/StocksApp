package com.SDE.stocksapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.models.Watchlist
import com.SDE.stocksapp.models.WatchlistStockCrossRef

@Database(
    entities = [Stock::class, Watchlist::class, WatchlistStockCrossRef::class],
    version = 1
)
abstract class StockDatabase : RoomDatabase() {

    abstract fun getStockDao(): StockDao

    companion object {
        @Volatile
        private var instance: StockDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                StockDatabase::class.java,
                "stock_db.db"
            ).build()
    }

}