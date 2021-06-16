package com.example.svrmaps.ui.exchange.select_subject

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrSelectSubjectBottomSheetBinding
import com.example.svrmaps.model.subject.Subject
import com.example.svrmaps.utils.registerOnBackPressedCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSubjectBottomSheetFragment : BottomSheetDialogFragment() {

    interface Listener {
        fun onSubjectSelected(subject: Subject)
    }

    private lateinit var _binding: FrSelectSubjectBottomSheetBinding
    private val binding get() = _binding

    private val subjectsAdapter by lazy {
        SelectSubjectAdapter {
            (parentFragment as? Listener)?.onSubjectSelected(it)
            dismiss()
        }
    }

    private val viewModel: SelectSubjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrSelectSubjectBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerOnBackPressedCallback {
            dismiss()
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.subjectsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = subjectsAdapter
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight: Int = resources.displayMetrics.heightPixels
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            subjectsData.subscribe {
                subjectsAdapter.submitList(it)
            }
        }
    }
}