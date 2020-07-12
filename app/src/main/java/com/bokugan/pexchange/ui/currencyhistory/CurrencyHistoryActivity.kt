package com.bokugan.pexchange.ui.currencyhistory

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.bokugan.pexchange.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyHistoryActivity : AppCompatActivity(R.layout.activity_currency_history) {

    private val vm by viewModels<CurrencyHistoryViewModel>()
}