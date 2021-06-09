package com.example.svrmaps.ui.map.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.FrMapFlowBinding
import com.example.svrmaps.ui.map.MapFragment
import com.example.svrmaps.utils.newRootScreen

class MapFlowFragment : Fragment() {

    private lateinit var _binding: FrMapFlowBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrMapFlowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.newRootScreen(
                MapFragment::class.java
            )
        }
    }
}