package com.example.svrmaps.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrProfileBinding
import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.ui.exchange.select_subject.SelectSubjectAdapter
import com.example.svrmaps.ui.exchange.select_subject.SelectSubjectBottomSheetFragment
import com.example.svrmaps.ui.profile.route.RouteFragment
import com.example.svrmaps.utils.navigateTo
import com.example.svrmaps.utils.registerOnBackPressedCallback
import com.example.svrmaps.utils.setSlideAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var _binding: FrProfileBinding
    private val binding get() = _binding

    private val viewModel: ProfileViewModel by viewModels()

    private val approvedExchangesAdapter by lazy {
        ApprovedExchangesAdapter {
            parentFragmentManager.navigateTo(
                RouteFragment::class.java,
                bundleOf(RouteFragment.ARG_EXCHANGE to it),
                setupFragmentTransaction = { it.setSlideAnimation() }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerOnBackPressedCallback {
            parentFragmentManager.popBackStack()
        }
        binding.subjectsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = approvedExchangesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            profileData.subscribe {
                binding.tvEmail.text = it
            }
            exchangesData.subscribe {
                approvedExchangesAdapter.submitList(it)
            }
        }
    }
}