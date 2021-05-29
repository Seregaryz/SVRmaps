package com.example.svrmaps.fragment.sign_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrSignInBinding
import com.example.svrmaps.databinding.FragmentMapBinding

class SignInFragment : Fragment() {

    private lateinit var _binding: FrSignInBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
    }
}