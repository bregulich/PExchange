package com.bokugan.pexchange.ui.currencyhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bokugan.pexchange.databinding.CurrencyPairItemBinding
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import java.text.DateFormat.getDateTimeInstance
import java.util.*

class CurrencyPairAdapter() : RecyclerView.Adapter<CurrencyPairViewHolder>() {

    var data: List<HistoricalCurrencyPair> = emptyList()
        set(value) {
            field = value ?: emptyList()
            notifyDataSetChanged()
        }
        get() {
            return field ?: emptyList()
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
            date.text = currencyPairItem.toLocalDateTime()
            currencyPair.text = currencyPairItem.baseQuote()
            buySell.text = currencyPairItem.buySell()
        }
    }

    companion object {
        private val formatter = getDateTimeInstance()

        private fun HistoricalCurrencyPair.toLocalDateTime() =
            formatter.format(Date(createdUTC))

        private fun HistoricalCurrencyPair.baseQuote() = "$baseCurrency/$quoteCurrency"

        private fun HistoricalCurrencyPair.buySell() = "$buy/$sell"
    }
}