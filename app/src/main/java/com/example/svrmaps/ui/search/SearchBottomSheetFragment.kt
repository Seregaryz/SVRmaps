package com.example.svrmaps.ui.search

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FragmentSearchBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.*

class SearchBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentSearchBottomSheetBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetBinding.inflate(layoutInflater)
        return binding.root    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchManager = SearchFactory.getInstance().createSearchManager(
            SearchManagerType.ONLINE
        )
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val point = Geometry.fromPoint(Point(59.95, 30.32))
                val searchSession = searchManager.submit(
                    query,
                    point,
                    SearchOptions(),
                    object: Session.SearchListener {
                        override fun onSearchResponse(p0: Response) {

                        }

                        override fun onSearchError(p0: com.yandex.runtime.Error) {

                        }
                    }
                )
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val point = Geometry.fromPoint(Point(59.95, 30.32))
                val searchSession = searchManager.submit(
                    newText,
                    point,
                    SearchOptions(),
                    object: Session.SearchListener {
                        override fun onSearchResponse(p0: Response) {
                            val cities = p0.collection.children.firstOrNull()?.obj
                                ?.metadataContainer
                                ?.getItem(ToponymObjectMetadata::class.java)
                                ?.address
                            Toast.makeText(requireContext(), p0.metadata.found.toString(), Toast.LENGTH_LONG).show()
                        }

                        override fun onSearchError(p0: com.yandex.runtime.Error) {

                        }
                    }
                )
                return false
            }
        })
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(
                @NonNull bottomSheet: View,
                newState: Int
            ) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(
                @NonNull bottomSheet: View,
                slideOffset: Float
            ) {
            }
        })
        val layoutParams = bottomSheet.layoutParams
        val windowHeight: Int = resources.displayMetrics.heightPixels
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

}