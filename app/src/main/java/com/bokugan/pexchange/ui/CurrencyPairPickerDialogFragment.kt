package com.bokugan.pexchange.ui

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bokugan.pexchange.R
import com.bokugan.pexchange.databinding.CurrencyPairPickerBinding
import com.bokugan.pexchange.entities.Currency

interface CurrencyPickerCallback {
    fun onNewCurrencyPair(baseCurrency: Currency, quoteCurrency: Currency)
}

private const val BASE_CURRENCY_KEY = "BASE_CURRENCY_KEY"
private const val QUOTE_CURRENCY_KEY = "QUOTE_CURRENCY_KEY"

class CurrencyPairPickerDialogFragment : DialogFragment() {

    private var _binding: CurrencyPairPickerBinding? = null
    private val binding get() = _binding!!

    private var baseCurrency: Currency = Currency.UAH
    private var quoteCurrency: Currency = Currency.USD

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        (savedInstanceState ?: arguments)?.let { bundle ->
            baseCurrency = Currency[bundle.getInt(BASE_CURRENCY_KEY)]
            quoteCurrency = Currency[bundle.getInt(QUOTE_CURRENCY_KEY)]
        }

        _binding = CurrencyPairPickerBinding.inflate(
            layoutInflater, null, false
        )

        with(binding) {
            baseCurrencyPicker.currency = baseCurrency
            quoteCurrencyPicker.currency = quoteCurrency
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.pick_currencies)
            .setPositiveButton(R.string.ok) { _, _ -> onNewCurrencyPair() }
            .setView(binding.root)
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(BASE_CURRENCY_KEY, Currency[binding.baseCurrencyPicker.currency.code].code)
            putInt(QUOTE_CURRENCY_KEY, Currency[binding.quoteCurrencyPicker.currency.code].code)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNewCurrencyPair() {
        (activity as? CurrencyPickerCallback)?.let { callback ->
            with(binding) {
                callback.onNewCurrencyPair(
                    baseCurrencyPicker.currency,
                    quoteCurrencyPicker.currency
                )
            }
        }
        dismiss()
    }

    companion object {
        fun create(
            fragmentManager: FragmentManager,
            baseCurrency: Currency,
            quoteCurrency: Currency
        ) {
            CurrencyPairPickerDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(BASE_CURRENCY_KEY, baseCurrency.code)
                    putInt(QUOTE_CURRENCY_KEY, quoteCurrency.code)
                }
            }.show(
                fragmentManager,
                CurrencyPairPickerDialogFragment::class.simpleName
            )
        }
    }
}