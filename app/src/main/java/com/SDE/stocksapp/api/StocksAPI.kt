package com.SDE.stocksapp.api

import com.SDE.stocksapp.models.GainerLoserApiResponse
import com.SDE.stocksapp.models.StockDetailsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksAPI {

    @GET("query?function=TOP_GAINERS_LOSERS")
    suspend fun getGainersLosers(
        @Query("apikey")
        apiKey: String = API_KEY
    ) : Response<GainerLoserApiResponse>

    @GET("query?function=OVERVIEW")
    suspend fun getStockDetails(
        @Query("symbol")
        symbol: String,
        @Query("apikey")
        apiKey: String = API_KEY
    ) : Response<StockDetailsResponse>
}