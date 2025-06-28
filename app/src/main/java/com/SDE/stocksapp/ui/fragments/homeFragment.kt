package com.SDE.stocksapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.SDE.stocksapp.R
import com.SDE.stocksapp.adapters.GenericStockAdapter
import com.SDE.stocksapp.databinding.FragmentHomeBinding
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.ui.StockViewModel
import com.SDE.stocksapp.ui.StocksActivity
import com.example.newsapp.util.Resource

class homeFragment : Fragment(R.layout.fragment_home) {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: StockViewModel
    lateinit var stockAdapterLoser: GenericStockAdapter
    lateinit var stockAdapterGainer: GenericStockAdapter

    private val TAG = "homeFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel = (activity as StocksActivity).viewModel

        setupRecyclerViewGainers()
        setupRecyclerViewLosers()

        var gainers: MutableList<Stock>
        var losers: MutableList<Stock>

        viewModel.topGainersLosers.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { gainersLosersResponse ->
                        if (gainersLosersResponse.top_gainers!=null && gainersLosersResponse.top_gainers.isNotEmpty()) {
                            // convert first 4 topGainer objects from TopGainer to Stock in gainers list
                            gainers = gainersLosersResponse.top_gainers.subList(0, 6).map { topGainer ->
                                Stock(
                                    ticker = topGainer.ticker,
                                    change_amount = topGainer.change_amount,
                                    change_percentage = topGainer.change_percentage,
                                    price = topGainer.price,
                                    volume = topGainer.volume,
//                                    urlToImage = null
                                )
                            } as MutableList<Stock>
                            stockAdapterGainer.differ.submitList(gainers)
                        } else {
                            // Handle case where top_gainers is null or empty (e.g., clear the adapter)
                            stockAdapterGainer.differ.submitList(emptyList())
                            if (gainersLosersResponse.top_gainers == null) {
                                Log.w(TAG, "top_gainers list is null in API response.")
                            }
                        }
                        if(gainersLosersResponse.top_losers!=null && gainersLosersResponse.top_losers.isNotEmpty()) {
                            losers = gainersLosersResponse.top_losers.subList(0, 6).map { topLoser ->
                                Stock(
                                    ticker = topLoser.ticker,
                                    change_amount = topLoser.change_amount,
                                    change_percentage = topLoser.change_percentage,
                                    price = topLoser.price,
                                    volume = topLoser.volume,
//                                    urlToImage = null
                                )
                            } as MutableList<Stock>
                            stockAdapterLoser.differ.submitList(losers)
                        } else {
                            // Handle case where top_losers is null or empty
                            stockAdapterLoser.differ.submitList(emptyList())
                            if (gainersLosersResponse.top_losers == null) {
                                Log.w(TAG, "top_losers list is null in API response.")
                            }
                        }
                    } ?: run {
                        // This block executes if response.data itself is null
                        Log.e(TAG, "API Response data (GainerLoserApiResponse) is null. Likely due to API limit or parsing error.")
                        stockAdapterGainer.differ.submitList(emptyList())
                        stockAdapterLoser.differ.submitList(emptyList())
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

        // Put setOnItemClickListener on both view all of gainers and losers to navigate to topGainersFragment and topLosersFragment respectively using action of stocks_nav_graph.xml
        binding.tvGainersViewAll.setOnClickListener {
            val action = homeFragmentDirections.actionHomeFragmentToTopGainersFragment()
            view.findNavController().navigate(action)
        }
        binding.tvLosersViewAll.setOnClickListener {
            val action = homeFragmentDirections.actionHomeFragmentToTopLosersFragment()
            view.findNavController().navigate(action)
        }
        stockAdapterGainer.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("stock", it)
            }
            view.findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,
                bundle
            )
        }
        stockAdapterLoser.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("stock", it)
            }
            view.findNavController().navigate(
                R.id.action_homeFragment_to_detailsFragment,
                bundle
            )
        }
    }

    private fun setupRecyclerViewGainers() {
        stockAdapterGainer = GenericStockAdapter()
        binding.rvGainersHome.apply {
            adapter = stockAdapterGainer
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewLosers() {
        stockAdapterLoser = GenericStockAdapter()
        binding.rvLosersHome.apply {
            adapter = stockAdapterLoser
            setHasFixedSize(true)
        }
    }

}