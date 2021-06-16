package com.example.svrmaps.ui.profile.route

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.svrmaps.BuildConfig
import com.example.svrmaps.R
import com.example.svrmaps.databinding.FrExchhangeMapBinding
import com.example.svrmaps.databinding.FrRouteBinding
import com.example.svrmaps.model.exchange.Exchange
import com.example.svrmaps.ui.base.BaseFragment
import com.example.svrmaps.ui.exchange.ExchangeMapFragment
import com.example.svrmaps.utils.registerOnBackPressedCallback
import com.github.florent37.runtimepermission.rx.RxPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.optimization.v1.MapboxOptimization
import com.mapbox.api.optimization.v1.models.OptimizationResponse
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
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RouteFragment : BaseFragment() {

    private lateinit var _binding: FrRouteBinding
    private val binding get() = _binding

    private val exchange: Exchange? by lazy {
        requireArguments().getParcelable<Exchange>(ARG_EXCHANGE)
    }

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
        _binding = FrRouteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerOnBackPressedCallback {
            parentFragmentManager.popBackStack()
        }
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
                initRoute()
                createRoute(listOf(
                    Point.fromLngLat(exchange?.offerLongitude?: 0.0, exchange?.offerLatitude ?: 0.0),
                    Point.fromLngLat(exchange?.destLongitude?: 0.0, exchange?.destLatitude ?: 0.0)
                ))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationComponent(mapboxMap: MapboxMap, style: Style) {

        mapboxMap.apply {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    val lat = location?.latitude ?: 0.0
                    val long = location?.longitude ?: 0.0
                    val bearing = location?.bearing ?: 0.0f
                    setLatLngBoundsForCameraTarget(
                        LatLngBounds.from(lat + 0.03, long + 0.15, lat - 0.03, long - 0.15)
                    )
                    setMinZoomPreference(ExchangeMapFragment.MIN_ZOOM)
                    setMaxZoomPreference(ExchangeMapFragment.MAX_ZOOM)
                    cameraPosition = CameraPosition.Builder()
                        .bearing(bearing.toDouble())
                        .target(LatLng(lat, long))
                        .zoom(ExchangeMapFragment.INITIAL_ZOOM)
                        .build()
                }
            
            val symbolLayerIconFeatureList: MutableList<Feature> = ArrayList()
            symbolLayerIconFeatureList.add(
                Feature.fromGeometry(
                    Point.fromLngLat(exchange?.offerLongitude ?: 0.0, exchange?.offerLatitude ?: 0.0)
                )
            )
            symbolLayerIconFeatureList.add(
                Feature.fromGeometry(
                    Point.fromLngLat(exchange?.destLongitude ?: 0.0, exchange?.destLatitude ?: 0.0)
                )
            )
            mapboxMap.setStyle(
                Style.Builder()
                    .fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                    .withImage(
                        ExchangeMapFragment.ICON_ID, BitmapFactory.decodeResource(
                            resources,
                            R.drawable.mapbox_marker_icon_default
                        )
                    ) // Adding a GeoJson source for the SymbolLayer icons.
                    .withSource(
                        GeoJsonSource(
                            ExchangeMapFragment.SOURCE_ID,
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
                        )
                    ) // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                    // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                    // the coordinate point. This is offset is not always needed and is dependent on the image
                    // that you use for the SymbolLayer icon.
                    .withLayer(
                        SymbolLayer(ExchangeMapFragment.LAYER_ID, ExchangeMapFragment.SOURCE_ID)
                            .withProperties(
                                PropertyFactory.iconImage(ExchangeMapFragment.ICON_ID),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true)
                            )
                    )
            ) {
                // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
            }
        }
    }

    private var currentRoute: DirectionsRoute? = null
    private val ROUTE_SOURCE_ID = "route-source-id"
    private val RED_PIN_ICON_ID = "red-pin-icon-id"

    private fun createRoute(points: List<Point>) {
        buildClient(points).enqueueCall(object : Callback<DirectionsResponse> {

            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                if (response.body() == null) {
                    return
                } else if (response.body()!!.routes().size < 1) {
                    return
                }

                currentRoute = response.body()!!.routes()[0]
                if (mapboxMap != null) {
                    if (navigationMapRoute != null) {
                        navigationMapRoute?.removeRoute()
                    } else {
                        navigationMapRoute = NavigationMapRoute(
                            null,
                            binding.mapView,
                            mapboxMap!!,
                            R.style.NavigationMapRoute
                        )
                    }
                    navigationMapRoute?.addRoute(currentRoute)
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                Timber.d(t.message.orEmpty())
            }

        })
    }

    private fun buildClient(points: List<Point>): MapboxDirections {
        val pointsSize = points.size
        val client = MapboxDirections.builder()
            .origin(points[0])
            .destination(points[pointsSize - 1])
            .steps(true)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .accessToken(getString(R.string.mapbox_access_token))
        return client.build()
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
        const val ARG_EXCHANGE = "exchange"
    }

}