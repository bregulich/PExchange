package com.bokugan.pexchange.ui.currencyhistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.bokugan.pexchange.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_currency_history.*

@AndroidEntryPoint
class CurrencyHistoryActivity : AppCompatActivity(R.layout.activity_currency_history) {

    private val vm by viewModels<CurrencyHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        recycler_view.apply {
            setHasFixedSize(true)
        }
    }
}