package com.SDE.stocksapp.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.SDE.stocksapp.R
import com.SDE.stocksapp.adapters.WatchlistCheckboxAdapter
import com.SDE.stocksapp.databinding.FragmentDetailsBinding
import com.SDE.stocksapp.databinding.DialogAddToWatchlistBinding
import com.SDE.stocksapp.databinding.FragmentHomeBinding
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.models.Watchlist
import com.SDE.stocksapp.ui.StockViewModel
import com.SDE.stocksapp.ui.StocksActivity
import com.example.newsapp.util.Resource

class detailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var binding : FragmentDetailsBinding
    lateinit var viewModel : StockViewModel
    val args : detailsFragmentArgs by navArgs()

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var dialogBinding: DialogAddToWatchlistBinding
    private val checkboxAdapter by lazy { WatchlistCheckboxAdapter() }

    private val TAG = "detailsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        viewModel = (activity as StocksActivity).viewModel
        val symbol = args.stock.ticker

        viewModel.getStockDetails(symbol)

        viewModel.stockDetails.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { stockDetailsResponse ->
                        if(stockDetailsResponse.Symbol != null) {
                            binding.apply {
                                tvCompanyName.text = stockDetailsResponse.Name
                                tvStockSymbol.text = "${stockDetailsResponse.Symbol} ,"
                                tvStockType.text = stockDetailsResponse.AssetType
                                tvExchange.text = stockDetailsResponse.Exchange
                                tvCurrentPrice.text = "$${args.stock.price}"
                                tvPriceChange.text = args.stock.change_percentage
                                tvAboutTitle.text = "About ${stockDetailsResponse.Name}"
                                tvAboutDescription.text = stockDetailsResponse.Description
                                tvIndustryTag.text = "Industry: ${stockDetailsResponse.Industry}"
                                tvSectorTag.text = "Sector: ${stockDetailsResponse.Sector}"
                                tvLowPrice.text = "$${stockDetailsResponse.`52WeekLow`}"
                                tvHighPrice.text = "$${stockDetailsResponse.`52WeekHigh`}"
                                tvCurrentPriceStat.text = "Current Price: $${args.stock.price}"
                                tvMarketCapValue.text = "$${stockDetailsResponse.MarketCapitalization}"
                                tvPERatioValue.text = "${stockDetailsResponse.PERatio}"
                                tvBetaValue.text = "${stockDetailsResponse.Beta}"
                                tvDividendYieldValue.text = "${stockDetailsResponse.DividendYield}"
                                tvProfitMarginValue.text = "${stockDetailsResponse.ProfitMargin}"
                                if(args.stock.change_percentage!="None")
                                {
                                    val percentage = args.stock.change_percentage.toDoubleOrNull() ?: 0.0
                                    tvPriceChange.setTextColor(
                                        ContextCompat.getColor(requireContext(),
                                            if (percentage>0) R.color.accent_green else R.color.accent_red)
                                    )
                                }
                            }

                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                    Log.d(TAG, "Loading...")
                }
            }
        })

        binding.ivBookmark.setOnClickListener {
            showAddToWatchlistSheet()
        }
    }

    private fun showAddToWatchlistSheet() {
        dialogBinding = DialogAddToWatchlistBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(dialogBinding.root)
            (dialogBinding.root.parent as View).setBackgroundResource(R.drawable.bottom_dialog_background)
        }

        dialogBinding.rvExistingWatchlists.apply {
            adapter = checkboxAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.watchlists.observe(viewLifecycleOwner) { lists ->
            checkboxAdapter.differ.submitList(lists)
        }

        dialogBinding.btnAddWatchlist.setOnClickListener {
            val name = dialogBinding.etNewWatchlistName.text.toString().trim()
            if (name.isNotEmpty()) {
                viewModel.saveWatchlist(name)
                dialogBinding.etNewWatchlistName.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        val selectedLists = mutableSetOf<String>()

        checkboxAdapter.setOnItemClickListener { watchlist ->
            if (selectedLists.contains(watchlist.watchlistName)) {
                selectedLists.remove(watchlist.watchlistName)
            } else {
                selectedLists.add(watchlist.watchlistName)
            }
        }

        bottomSheetDialog.show()

        dialogBinding.btnAddStock.setOnClickListener {
            if (selectedLists.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Select at least one watchlist",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.saveStockIntoWatchlists(args.stock, selectedLists.toList())
            Toast.makeText(requireContext(),
                "${args.stock.ticker} added to ${selectedLists.joinToString()}",
                Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }
    }
}