package com.bokugan.pexchange.ui.convertcurrency

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bokugan.pexchange.R
import com.bokugan.pexchange.databinding.ActivityConvertCurrencyBinding
import com.bokugan.pexchange.entities.Currency
import dagger.hilt.android.AndroidEntryPoint

private const val BASE_CURRENCY_KEY = "BASE_CURRENCY_KEY"
private const val QUOTE_CURRENCY_KEY = "QUOTE_CURRENCY_KEY"

@AndroidEntryPoint
class ConvertCurrencyActivity : AppCompatActivity() {

    private val vm by viewModels<ConvertCurrencyViewModel>()

    private lateinit var binding: ActivityConvertCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConvertCurrencyBinding
            .inflate(layoutInflater)
            .also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.currency, menu)
        return true
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