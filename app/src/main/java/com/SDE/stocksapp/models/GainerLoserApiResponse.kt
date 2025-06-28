package com.SDE.stocksapp.models

data class GainerLoserApiResponse(
    val last_updated: String,
    val metadata: String,
    val most_actively_traded: List<MostActivelyTraded>,
    val top_gainers: List<TopGainer>,
    val top_losers: List<TopLoser>
)