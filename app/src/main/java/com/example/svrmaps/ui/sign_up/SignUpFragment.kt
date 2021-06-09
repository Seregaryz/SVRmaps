package com.example.svrmaps.ui.sign_up

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.svrmaps.databinding.FrSignUpBinding
import com.example.svrmaps.system.subscribeToEvent
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.sign_in.SignInFragment
import com.example.svrmaps.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private lateinit var _binding: FrSignUpBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSignUpBinding.inflate(layoutInflater)
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
        registerOnBackPressedCallback {
            parentFragmentManager.popBackStack()
        }
        binding.etEmail.addTextChangedListener {
            viewModel.currentUserData.email = it.toString()
            binding.btnSignUp.isEnabled = viewModel.validateData()
        }
        binding.etPassword.addTextChangedListener {
            viewModel.currentUserData.password = it.toString()
            binding.btnSignUp.isEnabled = viewModel.validateData()
        }
        binding.etRepeatPassword.addTextChangedListener {
            viewModel.currentUserData.confirmPassword = it.toString()
            binding.btnSignUp.isEnabled = viewModel.validateData()
        }
        binding.btnSignUp.apply {
            isEnabled = false
            setOnClickListener {
                viewModel.createAccount()
            }
        }
        binding.btnSignIn.setOnClickListener {
            parentFragmentManager.replace(
                SignInFragment::class.java,
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
            successSignUp.subscribe {
                parentFragmentManager.navigateTo(
                    SignInFragment::class.java,
                    setupFragmentTransaction = { it.setSlideAnimation() }
                )
            }
        }
    }

}