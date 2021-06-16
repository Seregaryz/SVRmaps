package com.example.svrmaps.ui.add_subject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrExchangeSuccessBinding
import com.example.svrmaps.databinding.FrSuccessCreatingBinding
import com.example.svrmaps.ui.exchange.ExchangeOfferSuccessFragment

class SuccessCreatingFragment : Fragment() {

    interface OnNavigationListener {
        fun navigateToMap()
    }

    private lateinit var _binding: FrSuccessCreatingBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSuccessCreatingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToMap.setOnClickListener {
            (parentFragment as? OnNavigationListener)?.navigateToMap()
        }
    }
}