package com.SDE.stocksapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.SDE.stocksapp.adapters.GenericStockAdapter.StockViewHolder
import com.SDE.stocksapp.databinding.ItemStockCardBinding
import com.SDE.stocksapp.databinding.WatchlistCardBinding
import com.SDE.stocksapp.models.Stock
import com.SDE.stocksapp.models.Watchlist

class WatchlistAdapter :  RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    inner class WatchlistViewHolder(val binding: WatchlistCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Watchlist>() {
        override fun areItemsTheSame(oldItem: Watchlist, newItem: Watchlist): Boolean {
            return oldItem.watchlistName == newItem.watchlistName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Watchlist, newItem: Watchlist): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val binding = WatchlistCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val watchlist = differ.currentList[position]
        holder.binding.apply {
            tvWatchlistName.text = watchlist.watchlistName
            root.setOnClickListener {
                onItemClickListener?.let { it(watchlist) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Watchlist) -> Unit)? = null

    fun setOnItemClickListener(listener: (Watchlist) -> Unit) {
        onItemClickListener = listener
    }

}