package com.example.svrmaps.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var _binding: FrProfileBinding
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            profileData.subscribe {
                binding.tvEmail
            }
        }
    }
}