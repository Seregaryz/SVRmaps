package com.example.svrmaps.fragment.profile.flow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrProfileFlowBinding

class ProfileFlowFragment : Fragment() {

    private lateinit var _binding: FrProfileFlowBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrProfileFlowBinding.inflate(layoutInflater)
        return binding.root
    }

}