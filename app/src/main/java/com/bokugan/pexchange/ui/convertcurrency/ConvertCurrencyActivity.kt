package com.bokugan.pexchange.ui.convertcurrency

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import com.bokugan.pexchange.R
import com.bokugan.pexchange.databinding.ActivityConvertCurrencyBinding
import com.bokugan.pexchange.entities.Currency
import com.bokugan.pexchange.extensions.observeEvent
import com.bokugan.pexchange.ui.CurrencyPairPickerDialogFragment
import com.bokugan.pexchange.ui.CurrencyPickerCallback
import dagger.hilt.android.AndroidEntryPoint

private const val BASE_CURRENCY_KEY = "BASE_CURRENCY_KEY"
private const val QUOTE_CURRENCY_KEY = "QUOTE_CURRENCY_KEY"

@AndroidEntryPoint
class ConvertCurrencyActivity : AppCompatActivity(), CurrencyPickerCallback {

    private val vm by viewModels<ConvertCurrencyViewModel>()

    private lateinit var binding: ActivityConvertCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConvertCurrencyBinding
            .inflate(layoutInflater)
            .also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        vm.currencyPairRequest.observeEvent(this) {
            CurrencyPairPickerDialogFragment.create(
                supportFragmentManager,
                it.baseCurrency,
                it.quoteCurrency
            )
        }

        vm.baseCurrency.observe(this) {
            binding.baseCurrencyLabel.text = it.toString()
        }

        vm.quoteCurrency.observe(this) {
            binding.quoteCurrencyLabel.text = it.toString()
        }

        // TODO. Ugly. Two-way databinding + type converter to the rescue...
        vm.amount.observe(this) {
            val amountString = it.toString()
            val oldEditable = binding.amount.text
            if (amountString != oldEditable.toString()) {
                binding.amount.text = Editable.Factory().newEditable(amountString)
            }
        }

        binding.amount.addTextChangedListener {
            vm.updateAmount(it.toString().toDouble())
        }

        vm.quoteAmount.observe(this) {
            binding.quoteAmountLabel.text = it.toString()
        }

        if (savedInstanceState == null) {
            intent?.extras?.let {
                vm.updateCurrencyPair(
                    Currency[it.getInt(BASE_CURRENCY_KEY)],
                    Currency[it.getInt(QUOTE_CURRENCY_KEY)]
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.currency, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pick_currencies -> vm.requestUpdateCurrencyPair()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewCurrencyPair(baseCurrency: Currency, quoteCurrency: Currency) {
        vm.updateCurrencyPair(baseCurrency, quoteCurrency)
    }

    companion object {
        fun startActivity(caller: Activity, baseCurrency: Currency, quoteCurrency: Currency) {
            caller.startActivity(
                Intent(caller, ConvertCurrencyActivity::class.java).apply {
                    putExtra(BASE_CURRENCY_KEY, baseCurrency.code)
                    putExtra(QUOTE_CURRENCY_KEY, quoteCurrency.code)
                })
        }
    }
}