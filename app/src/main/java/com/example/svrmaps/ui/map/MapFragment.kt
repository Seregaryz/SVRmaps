package com.example.svrmaps.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FragmentMapBinding
import com.example.svrmaps.ui.search.SearchBottomSheetFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.search.Response
import com.yandex.runtime.image.ImageProvider
import timber.log.Timber


class MapFragment : Fragment() {

    private lateinit var _binding: FragmentMapBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.setApiKey("72f87e3c-f677-4e1a-b3b9-38e57bc22815")
        MapKitFactory.initialize(requireContext())
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.map.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
        binding.toolbar.apply {
            inflateMenu(R.menu.dashboard)
            setOnMenuItemClickListener {
                SearchBottomSheetFragment().show(childFragmentManager, null)
                true
            }
        }
    }

    fun onSearchResponse(response: Response) {
        val mapObjects: MapObjectCollection = binding.mapView.map.mapObjects
        mapObjects.clear()
        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj!!.geometry[0].point
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(context, R.drawable.ic_search_result)
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }


}