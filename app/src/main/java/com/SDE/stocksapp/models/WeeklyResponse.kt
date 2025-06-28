package com.SDE.stocksapp.models

import com.google.gson.annotations.SerializedName

data class WeeklyResponse(
    @SerializedName("Weekly Time Series")
    val timeSeries: Map<String, WeeklyCandle>
)
