package com.example.svrmaps.ui.exchange.exchange_offer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrExchangeOfferBottomSheetBinding
import com.example.svrmaps.databinding.FrExchhangeMapBinding
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.system.subscribeToEvent
import com.example.svrmaps.ui.exchange.select_subject.SelectSubjectBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeOfferBottomSheetFragment : BottomSheetDialogFragment(),
    SelectSubjectBottomSheetFragment.Listener {

    interface OnNavigationListener {
        fun makeExchange()
    }

    private lateinit var _binding: FrExchangeOfferBottomSheetBinding
    private val binding get() = _binding

    private val subject: Subject? by lazy {
        requireArguments().getParcelable<Subject>(ARG_SUBJECT)
    }

    private val viewModel: ExchangeOfferViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrExchangeOfferBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subjectName.text = subject?.name
        binding.subjectDescription.text = subject?.description
        binding.subjectUserName.text = subject?.creatorEmail
        binding.selectSubjectButton.setOnClickListener {
            SelectSubjectBottomSheetFragment().show(childFragmentManager, null)
        }
        binding.btnMakeExchange.apply {
            isEnabled = false
            setOnClickListener {
                viewModel.createExchangeOffer(subject)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            loading.subscribe {
            }
            errorMessage.subscribeToEvent {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            successCreating.subscribe {
                (parentFragment as? OnNavigationListener)?.makeExchange()
                dismiss()
            }
        }
    }

    companion object {
        const val ARG_SUBJECT = "subject"
        fun create(subject: Subject) = ExchangeOfferBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SUBJECT, subject)
            }
        }
    }

    override fun onSubjectSelected(subject: Subject) {
        viewModel.currentOfferSubject = subject
        binding.selectSubjectButton.apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_text))
            text = subject.name
        }
        binding.btnMakeExchange.isEnabled = true
    }

}