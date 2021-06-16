package com.example.svrmaps.ui.add_subject

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.svrmaps.databinding.FrAddSubjectBinding
import com.example.svrmaps.system.subscribeToEvent
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.utils.hideKeyboard
import com.example.svrmaps.utils.navigateTo
import com.example.svrmaps.utils.setSlideAnimation
import com.github.florent37.runtimepermission.rx.RxPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class AddSubjectFragment : BaseFragment() {

    private lateinit var _binding: FrAddSubjectBinding
    private val binding get() = _binding

    private var permissionDisposable: Disposable? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
        requestLocationPermissions()
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
            successCreating.subscribe {
                parentFragmentManager.navigateTo(
                    SuccessCreatingFragment::class.java,
                    setupFragmentTransaction = { it.setSlideAnimation() }
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationPermissions() {
        permissionDisposable = RxPermissions(this).request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
            .subscribe({ result ->
                if (result.isAccepted) {
                    binding.btnCreate.apply {
                        isEnabled = false
                        setOnClickListener {
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { location : Location? ->
                                    viewModel.createSubject(location?.latitude, location?.longitude)
                                    // Got last known location. In some rare situations this can be null.
                                }
                        }
                    }
                }
            }, { throwable ->
                val result = (throwable as RxPermissions.Error).result
                if (result.hasDenied()) {
                    //the list of denied permissions
                    result.denied.forEach {
                        Toast.makeText(requireContext(), "Denied: $it", Toast.LENGTH_SHORT).show()
                    }
                    //permission denied, but you can ask again, eg:
                    AlertDialog.Builder(requireContext()).apply {
                        setMessage("Please accept our permissions")
                        setPositiveButton("Ok") { _, _ ->
                            result.askAgain()
                        }
                        setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create().show()
                }
                if (result.hasForeverDenied()) {
                    result.foreverDenied.forEach {
                        Toast.makeText(requireContext(), "Forever denied: $it", Toast.LENGTH_SHORT).show()
                    }
                    result.goToSettings()
                }

            })
    }


}