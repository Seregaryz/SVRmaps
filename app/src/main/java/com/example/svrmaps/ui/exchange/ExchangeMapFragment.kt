package com.example.svrmaps.ui.exchange

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrExchhangeMapBinding
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.exchange.exchange_offer.ExchangeOfferBottomSheetFragment
import com.github.florent37.runtimepermission.rx.RxPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable


@AndroidEntryPoint
class ExchangeMapFragment : BaseFragment() {

    private lateinit var _binding: FrExchhangeMapBinding
    private val binding get() = _binding

    private val viewModel: ExchangeViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var navigationMapRoute: NavigationMapRoute? = null
    private var mapboxMap: MapboxMap? = null
    private var routes: List<DirectionsRoute>? = null

    private var permissionDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
        _binding = FrExchhangeMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.mapView.onCreate(savedInstanceState)
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        permissionDisposable = RxPermissions(this).request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
            .subscribe({ result ->
                if (result.isAccepted) {
                    initMapView()
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
                        Toast.makeText(requireContext(), "Forever denied: $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                    result.goToSettings()
                }

            })
    }

    private fun initMapView() {
        binding.mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                initLocationComponent(mapboxMap, it)
                this.mapboxMap = mapboxMap
                viewModel.getSubjects()
                initRoute()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationComponent(mapboxMap: MapboxMap, style: Style) {
        val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
            .elevation(5f)
            .accuracyAlpha(.6f)
            .accuracyColor(Color.TRANSPARENT)
            .foregroundDrawable(R.drawable.ic_launcher_foreground)
            .build()

        mapboxMap.apply {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val lat = location?.latitude ?: 0.0
                    val long = location?.longitude ?: 0.0
                    val bearing = location?.bearing ?: 0.0f
                    setLatLngBoundsForCameraTarget(
                        LatLngBounds.from(lat + 0.03, long + 0.15, lat - 0.03, long - 0.15))
                    setMinZoomPreference(MIN_ZOOM)
                    setMaxZoomPreference(MAX_ZOOM)
                    cameraPosition = CameraPosition.Builder()
                        .bearing(bearing.toDouble())
                        .target(LatLng(lat, long))
                        .zoom(INITIAL_ZOOM)
                        .build()
                }

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), style)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }
        }
    }

    private fun initRoute() {
        routes?.let {
            if (navigationMapRoute != null) {
                navigationMapRoute?.removeRoute()
            } else {
                mapboxMap?.let { map ->
                    navigationMapRoute =
                        NavigationMapRoute(null, binding.mapView, map, R.style.NavigationMapRoute)
                }
            }
            navigationMapRoute?.addRoutes(it)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            subjectsData.subscribe {
                Toast.makeText(requireContext(), "Size: ${it.size}", Toast.LENGTH_SHORT).show()
                val symbolLayerIconFeatureList: MutableList<Feature> = ArrayList()
                it.forEach { subject ->
                    symbolLayerIconFeatureList.add(
                        Feature.fromGeometry(
                            Point.fromLngLat(subject.longitude ?: 0.0, subject.latitude ?: 0.0)
                        )
                    )
                }
                mapboxMap?.setStyle(
                    Style.Builder()
                        .fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                        .withImage(
                            ICON_ID, BitmapFactory.decodeResource(
                                resources,
                                R.drawable.mapbox_marker_icon_default
                            )
                        ) // Adding a GeoJson source for the SymbolLayer icons.
                        .withSource(
                            GeoJsonSource(
                                SOURCE_ID,
                                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
                            )
                        ) // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                        // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                        // the coordinate point. This is offset is not always needed and is dependent on the image
                        // that you use for the SymbolLayer icon.
                        .withLayer(
                            SymbolLayer(LAYER_ID, SOURCE_ID)
                                .withProperties(
                                    iconImage(ICON_ID),
                                    iconAllowOverlap(true),
                                    iconIgnorePlacement(true)
                                )
                        )
                ) {
                    // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
                }
                mapboxMap?.addOnMapClickListener { point ->
                    val offer = it.find { subject ->
                        subject.longitude!! < point.longitude + 0.001 && subject.longitude > point.longitude - 0.001 &&
                                subject.latitude!! < point.latitude + 0.001 && subject.latitude > point.latitude - 0.001
                    }
                    if (offer != null) {
                        ExchangeOfferBottomSheetFragment.create(offer).show(childFragmentManager, null)
                    }
                    true
                }
            }
        }
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        permissionDisposable?.dispose()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    companion object {
        const val MIN_ZOOM = 4.0
        const val MAX_ZOOM = 18.0
        const val INITIAL_ZOOM = 12.0
        const val SOURCE_ID = "SOURCE_ID"
        const val ICON_ID = "ICON_ID"
        const val LAYER_ID = "LAYER_ID"
    }
}