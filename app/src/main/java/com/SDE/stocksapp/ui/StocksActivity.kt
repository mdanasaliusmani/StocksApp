package com.SDE.stocksapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.SDE.stocksapp.R
import com.SDE.stocksapp.databinding.ActivityStocksBinding
import com.SDE.stocksapp.db.StockDatabase
import com.SDE.stocksapp.repository.StockRepository

class StocksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStocksBinding
    lateinit var viewModel: StockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = StockRepository(StockDatabase(this))
        val viewModelProviderFactory = StockViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(StockViewModel::class.java)

        binding = ActivityStocksBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.stocksNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}