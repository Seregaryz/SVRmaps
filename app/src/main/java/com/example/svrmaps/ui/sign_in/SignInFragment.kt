package com.example.svrmaps.ui.sign_in

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.svrmaps.databinding.FrSignInBinding
import com.example.svrmaps.system.subscribeToEvent
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.main.MainFragment
import com.example.svrmaps.ui.sign_up.SignUpFragment
import com.example.svrmaps.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    private lateinit var _binding: FrSignInBinding
    private val binding get() = _binding

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.setOnTouchListener { v, event ->
            v.onTouchEvent(event)
            hideKeyboard()
            true
        }
        binding.etEmail.addTextChangedListener {
            viewModel.currentEmail = it.toString()
            binding.btnSignIn.isEnabled = viewModel.validateData()
        }
        binding.etPassword.addTextChangedListener {
            viewModel.currentPassword = it.toString()
            binding.btnSignIn.isEnabled = viewModel.validateData()
        }
        binding.btnSignIn.apply {
            isEnabled = false
            setOnClickListener {
                viewModel.signIn()
            }
        }
        binding.btnSignUp.setOnClickListener {
            parentFragmentManager.replace(
                SignUpFragment::class.java,
                setupFragmentTransaction = { it.setSlideAnimation() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            loading.subscribe {
                showProgressDialog(it)
            }
            errorMessage.subscribeToEvent {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            successSignIn.subscribe {
                parentFragmentManager.newRootScreen(
                    MainFragment::class.java,
                    setupFragmentTransaction = { it.setSlideAnimation() }
                )
            }
        }
    }


    companion object {
    }
}