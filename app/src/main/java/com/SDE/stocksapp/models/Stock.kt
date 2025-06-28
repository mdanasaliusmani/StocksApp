package com.SDE.stocksapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Stock(
    @PrimaryKey(autoGenerate = false)
    val ticker: String,
    val change_amount: String,
    val change_percentage: String,
    val price: String,
    val volume: String,
//    val urlToImage: String?,
) : Serializable