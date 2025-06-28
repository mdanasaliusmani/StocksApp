package com.SDE.stocksapp.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WatchlistWithStock(
    @Embedded val watchlist: Watchlist,
    @Relation(
        parentColumn = "watchlistName",
        entityColumn = "ticker",
        associateBy = Junction(WatchlistStockCrossRef::class)
    )
    val stocks: List<Stock>
)