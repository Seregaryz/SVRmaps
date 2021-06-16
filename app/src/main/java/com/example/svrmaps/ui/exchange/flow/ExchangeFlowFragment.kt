package com.example.svrmaps.ui.exchange.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.FrExchangeFlowBinding
import com.example.svrmaps.databinding.LayoutContainerBinding
import com.example.svrmaps.ui.add_subject.AddSubjectFragment
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.exchange.ExchangeMapFragment
import com.example.svrmaps.ui.exchange.ExchangeOfferSuccessFragment
import com.example.svrmaps.utils.newRootScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeFlowFragment : BaseFragment(),
    ExchangeOfferSuccessFragment.OnNavigationListener {

    interface OnNavigationListener {
        fun navigateToProfile()
    }

    private lateinit var _binding: LayoutContainerBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.newRootScreen(
                ExchangeMapFragment::class.java
            )
        }
    }

    override fun navigateToProfile() {
        (parentFragment as? OnNavigationListener)?.navigateToProfile()
    }

}