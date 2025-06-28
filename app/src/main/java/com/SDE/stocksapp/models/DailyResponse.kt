package com.SDE.stocksapp.models

import com.google.gson.annotations.SerializedName

data class DailyResponse(
    @SerializedName("Time Series (Daily)")
    val timeSeries: Map<String, DailyCandle>
)
