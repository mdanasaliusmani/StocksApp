package com.SDE.stocksapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.SDE.stocksapp.databinding.ItemStockCardBinding
import com.SDE.stocksapp.models.Stock
import com.bumptech.glide.Glide

class GenericStockAdapter : RecyclerView.Adapter<GenericStockAdapter.StockViewHolder>() {

    inner class StockViewHolder(val binding: ItemStockCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.ticker == newItem.ticker
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = differ.currentList[position]
        holder.binding.apply {
//            if(stock.urlToImage != null) {
//                Glide.with(holder.itemView).load(stock.urlToImage).into(ivStockIcon)
//            }
            tvStockName.text = stock.ticker
            tvStockPrice.text = stock.price
            root.setOnClickListener {
                onItemClickListener?.let { it(stock) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Stock) -> Unit)? = null

    fun setOnItemClickListener(listener: (Stock) -> Unit) {
        onItemClickListener = listener
    }
}
