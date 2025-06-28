package com.SDE.stocksapp.models

import androidx.room.Entity

@Entity(primaryKeys = ["watchlistName", "ticker"])
data class WatchlistStockCrossRef(
    val watchlistName: String,
    val ticker: String
)