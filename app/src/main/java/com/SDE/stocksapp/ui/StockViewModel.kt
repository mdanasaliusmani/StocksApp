package com.SDE.stocksapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.SDE.stocksapp.StocksApplication
import com.SDE.stocksapp.models.GainerLoserApiResponse
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.models.StockDetailsResponse
import com.SDE.stocksapp.models.Watchlist
import com.SDE.stocksapp.models.WatchlistStockCrossRef
import com.SDE.stocksapp.repository.StockRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class StockViewModel(
    app: Application,
    val repository: StockRepository
) : AndroidViewModel(app) {

    val topGainersLosers: MutableLiveData<Resource<GainerLoserApiResponse>> = MutableLiveData()
    val stockDetails: MutableLiveData<Resource<StockDetailsResponse>> = MutableLiveData()
    val watchlists: LiveData<List<Watchlist>> = getAllWatchlists()

    init {
        getTopGainersLosers()
    }

    fun getTopGainersLosers() = viewModelScope.launch {
        topGainersLosers.postValue(Resource.Loading())
        safeGetTopGainersLosersCall()
    }

    fun getStockDetails(symbol: String) = viewModelScope.launch {
        stockDetails.postValue(Resource.Loading())
        safeGetStockDetailsCall(symbol)
    }

    private fun handleTopGainersLosersResponse(response: Response<GainerLoserApiResponse>) : Resource<GainerLoserApiResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleStockDetailsResponse(response: Response<StockDetailsResponse>) : Resource<StockDetailsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeGetTopGainersLosersCall() {
        topGainersLosers.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getTopGainersLosers()
                topGainersLosers.postValue(handleTopGainersLosersResponse(response))
            } else {
                topGainersLosers.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> topGainersLosers.postValue(Resource.Error("Network Failure"))
                else -> topGainersLosers.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeGetStockDetailsCall(symbol: String) {
        stockDetails.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getStockDetails(symbol)
                stockDetails.postValue(handleStockDetailsResponse(response))
            } else {
                stockDetails.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> stockDetails.postValue(Resource.Error("Network Failure"))
                else -> stockDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<StocksApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun saveStockIntoWatchlists(stock: Stock, watchlists: List<String>) = viewModelScope.launch {
        repository.insertStock(stock)
        watchlists.forEach { watchlistName ->
            repository.insertWatchlistStockCrossRef(WatchlistStockCrossRef(watchlistName, stock.ticker))
        }
    }

    fun saveWatchlist(watchlistName: String) = viewModelScope.launch {
        repository.insertWatchlist(Watchlist(watchlistName))
    }

    fun getAllWatchlists() = repository.getWatchlists()

    fun getStocksForWatchlist(watchlistName: String) = repository.getStocksForWatchlist(watchlistName)

    fun deleteWatchlist(watchlist: Watchlist) = viewModelScope.launch {
        repository.deleteWatchlist(watchlist)
    }

    fun deleteStockFromWatchlist(stock: Stock) = viewModelScope.launch {
        repository.deleteStockWatchlists(stock.ticker)
    }

}