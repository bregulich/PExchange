package com.bokugan.pexchange.ui

import android.app.Dialog
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bokugan.pexchange.R
import com.bokugan.pexchange.databinding.CurrencyPairPickerBinding

class CurrencyPairPickerDialogFragment : DialogFragment() {

    private var _binding: CurrencyPairPickerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.let {

        }

        _binding = CurrencyPairPickerBinding.inflate(
            layoutInflater, null, false
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.pick_currencies)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setView(binding.root)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}