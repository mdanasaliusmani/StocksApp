package com.SDE.stocksapp.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StockWithWatchlist(
    @Embedded val stock: Stock,
    @Relation(
        parentColumn = "ticker",
        entityColumn = "watchlistName",
        associateBy = Junction(WatchlistStockCrossRef::class)
    )
    val watchlists: List<Watchlist>
)