package com.SDE.stocksapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.SDE.stocksapp.databinding.WatchlistCheckboxBinding
import com.SDE.stocksapp.models.Watchlist

class WatchlistCheckboxAdapter : RecyclerView.Adapter<WatchlistCheckboxAdapter.WatchlistViewHolder>() {

    inner class WatchlistViewHolder(val binding: WatchlistCheckboxBinding) :
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
        val binding = WatchlistCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val watchlist = differ.currentList[position]
        holder.binding.apply {
            tvWatchlist.text = watchlist.watchlistName
                cbWatchlist.isChecked = setOnClickListener {
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