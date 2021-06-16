package com.example.svrmaps.ui.exchange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrAddSubjectBinding
import com.example.svrmaps.databinding.FrExchangeOfferBottomSheetBinding
import com.example.svrmaps.databinding.FrExchangeSuccessBinding

class ExchangeOfferSuccessFragment : Fragment() {

    interface OnNavigationListener {
        fun navigateToProfile()
    }

    private lateinit var _binding: FrExchangeSuccessBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrExchangeSuccessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToProfile.setOnClickListener {
            (parentFragment as? OnNavigationListener)?.navigateToProfile()
        }
    }

}