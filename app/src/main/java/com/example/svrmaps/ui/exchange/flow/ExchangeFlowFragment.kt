package com.example.svrmaps.ui.exchange.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.FrExchangeFlowBinding
import com.example.svrmaps.databinding.LayoutContainerBinding
import com.example.svrmaps.ui.base.BaseFragment

class ExchangeFlowFragment : BaseFragment() {

    private lateinit var _binding: LayoutContainerBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutContainerBinding.inflate(layoutInflater)
        return binding.root
    }

}