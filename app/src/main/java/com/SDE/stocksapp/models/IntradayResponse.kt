package com.SDE.stocksapp.models

import com.google.gson.annotations.SerializedName

data class IntradayResponse(
    @SerializedName("Time Series (5min)")
    val timeSeries: Map<String, IntradayCandle>
)
