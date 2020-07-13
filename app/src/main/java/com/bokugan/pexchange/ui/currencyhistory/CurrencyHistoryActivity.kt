package com.bokugan.pexchange.ui.currencyhistory

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe

import com.bokugan.pexchange.R
import com.bokugan.pexchange.databinding.ActivityCurrencyHistoryBinding
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.extensions.baseQuote
import com.bokugan.pexchange.extensions.buySell
import com.bokugan.pexchange.ui.CurrencyPairPickerDialogFragment
import com.bokugan.pexchange.ui.CurrencyPickerCallback
import com.bokugan.pexchange.usecases.HistoricalCurrencyPair
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import com.bokugan.pexchange.extensions.observeEvent
import com.bokugan.pexchange.ui.convertcurrency.ConvertCurrencyActivity


@AndroidEntryPoint
class CurrencyHistoryActivity : AppCompatActivity(), CurrencyPickerCallback {

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

        binding.chart.legend.isEnabled = false

        vm.currencyHistory.observe(this) {
            updateChart(it)
            updateRecyclerView(it)
        }

        vm.latestCurrencyPair.observe(this) {
            updateToolbarTitle(it)
        }

        vm.pickCurrencyPairRequest.observeEvent(this) {
            CurrencyPairPickerDialogFragment.create(
                supportFragmentManager,
                it.baseCurrency,
                it.quoteCurrency
            )
        }

        vm.convertCurrencyRequest.observeEvent(this) {
            ConvertCurrencyActivity.startActivity(
                this, it.baseCurrency, it.quoteCurrency
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.currency, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pick_currencies -> vm.requestNewCurrencyPair()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onConvertCurrencyClick(view: View) {
        vm.requestConvertCurrency()
    }

    private fun updateChart(list: List<HistoricalCurrencyPair>) {
        val entries = mutableListOf<Entry>()
        for (idx in list.indices) {
            val item = list[idx]
            entries.add(Entry(idx.toFloat(), item.buy.toFloat(), item))
        }

        val lineData = LineData(LineDataSet(entries, ""))
        with(binding.chart) {
            data = lineData
            invalidate()
        }
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

    override fun onNewCurrencyPair(baseCurrency: Currency, quoteCurrency: Currency) {
        vm.updateCurrencyHistory(baseCurrency, quoteCurrency)
    }
}