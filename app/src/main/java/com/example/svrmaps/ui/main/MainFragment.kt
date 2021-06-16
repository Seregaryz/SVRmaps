package com.example.svrmaps.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrMainBinding
import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.ui.add_subject.flow.AddSubjectFlowFragment
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.exchange.flow.ExchangeFlowFragment
import com.example.svrmaps.ui.profile.flow.ProfileFlowFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fr_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment(),
    ExchangeFlowFragment.OnNavigationListener,
    AddSubjectFlowFragment.OnNavigationListener {

    private var _binding: FrMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exchanges -> {
                    showFragment(exchangesTabKey)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.addNewSubject -> {
                    showFragment(addSubjectTabKey)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    showFragment(profileTabKey)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomBar = binding.bottomBar
        bottomBar.menu.clear()
        bottomBar.inflateMenu(R.menu.main_bottom)
        bottomBar.apply {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.exchanges
            viewModel.currentSelectedItemId = R.id.exchanges
            isItemHorizontalTranslationEnabled = false
        }
        showFragment(exchangesTabKey)
    }

    private fun showFragment(fragmentKey: String) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(fragmentKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) {
            return
        }
        childFragmentManager.beginTransaction()
            .apply {
                if (newFragment == null) {
                    Timber.d("newFragment is null")
                    add(R.id.mainScreenContainer, createTabFragment(fragmentKey), null, fragmentKey)
                }

                currentFragment?.let {
                    detach(it)
                }
                newFragment?.let {
                    attach(it)
                }
                setReorderingAllowed(true)
                commitNow()
            }
    }

    private fun createTabFragment(tabKey: String): Class<out BaseFragment> =
        when (tabKey) {
            exchangesTabKey -> ExchangeFlowFragment::class.java
            addSubjectTabKey -> AddSubjectFlowFragment::class.java
            else -> ProfileFlowFragment::class.java
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val exchangesTabKey = "exchangesFlowFragment"
        private const val addSubjectTabKey = "AddSubjectFlowFragment"
        private const val profileTabKey = "ProfileFragment"
    }

    override fun navigateToProfile() {
        binding.bottomBar.apply {
            selectedItemId = R.id.profile
            viewModel.currentSelectedItemId = R.id.profile
        }
        showFragment(profileTabKey)
    }

    override fun navigateToMap() {
        binding.bottomBar.apply {
            selectedItemId = R.id.exchanges
            viewModel.currentSelectedItemId = R.id.exchanges
        }
        showFragment(exchangesTabKey)
    }

}