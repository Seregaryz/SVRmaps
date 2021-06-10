package com.example.svrmaps.ui.add_subject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrAddSubjectBinding
import com.example.svrmaps.databinding.FragmentMapBinding
import com.example.svrmaps.system.subscribeToEvent
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.sign_in.SignInFragment
import com.example.svrmaps.utils.hideKeyboard
import com.example.svrmaps.utils.navigateTo
import com.example.svrmaps.utils.setSlideAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSubjectFragment : BaseFragment() {

    private lateinit var _binding: FrAddSubjectBinding
    private val binding get() = _binding

    private val viewModel: AddSubjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrAddSubjectBinding.inflate(layoutInflater)
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
        binding.etSubjectName.addTextChangedListener {
            viewModel.currentName = it.toString()
            binding.btnCreate.isEnabled = viewModel.validateData()
        }
        binding.etSubjectDescription.addTextChangedListener {
            viewModel.currentDescription = it.toString()
            binding.btnCreate.isEnabled = viewModel.validateData()
        }
        binding.btnCreate.apply {
            isEnabled = false
            setOnClickListener {
                viewModel.createSubject()
            }
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
                Toast.makeText(requireContext(), "Success: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

}