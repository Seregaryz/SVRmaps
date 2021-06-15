package com.example.svrmaps.ui.profile.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.FrProfileFlowBinding
import com.example.svrmaps.databinding.LayoutContainerBinding
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.exchange.ExchangeMapFragment
import com.example.svrmaps.ui.profile.ProfileFragment
import com.example.svrmaps.utils.newRootScreen

class ProfileFlowFragment : BaseFragment() {

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
                ProfileFragment::class.java
            )
        }
    }

}