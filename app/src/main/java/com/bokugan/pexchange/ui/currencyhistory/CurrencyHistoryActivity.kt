package com.bokugan.pexchange.ui.currencyhistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.bokugan.pexchange.R

import com.bokugan.pexchange.databinding.ActivityCurrencyHistoryBinding

import com.bokugan.pexchange.extensions.baseQuote
import com.bokugan.pexchange.extensions.buySell
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyHistoryActivity : AppCompatActivity() {

    private val vm by viewModels<CurrencyHistoryViewModel>()

    private lateinit var binding: ActivityCurrencyHistoryBinding

    private lateinit var currencyPairAdapter: CurrencyPairAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyHistoryBinding
            .inflate(layoutInflater)
            .also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        currencyPairAdapter = CurrencyPairAdapter()

        binding.recyclerView.apply {
            adapter = currencyPairAdapter
        }

        vm.currencyHistory.observe(this) {
            updateChart(it)
            updateRecyclerView(it)
        }

        vm.latestCurrencyPair.observe(this) {
            updateToolbarTitle(it)
        }
    }

    private fun updateChart(list: List<HistoricalCurrencyPair>) {

    }

    private fun updateRecyclerView(list: List<HistoricalCurrencyPair>) {
        currencyPairAdapter.data = list
    }

    private fun updateToolbarTitle(currencyPair: HistoricalCurrencyPair?) {
        binding.collapsingToolbarLayout.title =
            if (currencyPair == null) {
                resources.getString(R.string.n_a)
            } else {
                "${currencyPair.baseQuote} ${currencyPair.buySell}"
            }
    }
}