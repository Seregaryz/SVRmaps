package com.example.svrmaps.ui.add_subject.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.svrmaps.databinding.LayoutContainerBinding
import com.example.svrmaps.ui.add_subject.AddSubjectFragment
import com.example.svrmaps.ui.add_subject.SuccessCreatingFragment
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.utils.newRootScreen

class AddSubjectFlowFragment : BaseFragment(),
    SuccessCreatingFragment.OnNavigationListener {

    interface OnNavigationListener {
        fun navigateToMap()
    }

    private lateinit var _binding: LayoutContainerBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutContainerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager.newRootScreen(
                AddSubjectFragment::class.java
            )
        }
    }

    override fun navigateToMap() {
        (parentFragment as? OnNavigationListener)?.navigateToMap()
    }

}