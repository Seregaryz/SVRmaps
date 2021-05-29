package com.example.svrmaps.fragment.sign_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrSignUpBinding
import com.example.svrmaps.databinding.FragmentMapBinding


class SignUpFragment : Fragment() {

    private lateinit var _binding: FrSignUpBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {

    }
}