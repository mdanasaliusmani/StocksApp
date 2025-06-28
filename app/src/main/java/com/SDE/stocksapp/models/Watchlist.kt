package com.SDE.stocksapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Watchlist (
    @PrimaryKey(autoGenerate = false)
    val watchlistName: String
)