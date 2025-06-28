package com.SDE.stocksapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.SDE.stocksapp.repository.StockRepository

class StockViewModelProviderFactory(
    private val Application: Application,
    private val StockRepository: StockRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(app = Application, repository = StockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}