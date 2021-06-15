package com.example.svrmaps.ui.exchange.exchange_offer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrExchangeOfferBottomSheetBinding
import com.example.svrmaps.databinding.FrExchhangeMapBinding
import com.example.svrmaps.model.subject.Subject
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExchangeOfferBottomSheetFragment : BottomSheetDialogFragment() {

    interface OnNavigationListener {
        fun makeExchange()
    }

    private lateinit var _binding: FrExchangeOfferBottomSheetBinding
    private val binding get() = _binding

    private val subject: Subject? by lazy {
        requireArguments().getParcelable<Subject>(ARG_SUBJECT)
    }

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
        binding.btnMakeExchange.setOnClickListener {

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

}