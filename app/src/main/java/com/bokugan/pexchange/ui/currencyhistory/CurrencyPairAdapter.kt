package com.bokugan.pexchange.ui.currencyhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bokugan.pexchange.databinding.CurrencyPairItemBinding
import com.bokugan.pexchange.extensions.baseQuote
import com.bokugan.pexchange.extensions.buySell
import com.bokugan.pexchange.extensions.localDateTime
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair

class CurrencyPairAdapter() : RecyclerView.Adapter<CurrencyPairViewHolder>() {

    var data: List<HistoricalCurrencyPair> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyPairViewHolder(
            CurrencyPairItemBinding.inflate(
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                        as @org.jetbrains.annotations.NotNull LayoutInflater,
                parent,
                false
            )
        )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CurrencyPairViewHolder, position: Int) {
        holder.update(data[position])
    }
}

class CurrencyPairViewHolder(private val binding: CurrencyPairItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(currencyPairItem: HistoricalCurrencyPair) {
        // TODO. ViewModel (Rx/Transformations) work.
        with(binding) {
            date.text = currencyPairItem.localDateTime
            currencyPair.text = currencyPairItem.baseQuote
            buySell.text = currencyPairItem.buySell
        }
    }
}