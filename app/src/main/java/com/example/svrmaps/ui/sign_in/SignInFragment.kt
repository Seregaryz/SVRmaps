package com.example.svrmaps.ui.sign_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.FrSignInBinding
import com.example.svrmaps.ui.sign_up.SignUpFragment
import com.example.svrmaps.utils.replace
import com.example.svrmaps.utils.setSlideAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            parentFragmentManager.replace(
                SignUpFragment::class.java,
                setupFragmentTransaction = { it.setSlideAnimation() }
            )
        }
    }


    companion object {
    }
}