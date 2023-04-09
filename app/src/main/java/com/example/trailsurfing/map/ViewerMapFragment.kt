package com.example.trailsurfing.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trailsurfing.MainActivity
import com.example.trailsurfing.databinding.ViewerMapFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class ViewerMapFragment : Fragment() {
    private lateinit var binding: ViewerMapFragmentBinding
    private var position = LatLng(44.7243900, 37.7675200)
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewerMapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            val directions = ViewerMapFragmentDirections.actionViewerMapFragmentToFragmentPathCreate()

            findNavController().navigate(directions)
        }
        with(binding.map) {
            onCreate(null)
            getMapAsync {
                MapsInitializer.initialize(requireContext())

                googleMap = it

                mapReady()
            }
        }


    }

    @SuppressLint("MissingPermission")
    fun mapReady() {
        googleMap.clear()


        with(googleMap) {
            isMyLocationEnabled = true

            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))

            setOnMapClickListener {
                //insert code here
            }
        }

        if (!MainActivity.path.isNullOrEmpty()){
            val polylineOptions = PolylineOptions().apply {
                width(5f)
                color(Color.GRAY)
                geodesic(true)
            }
            var count = 0
            var lat = 0.0
            var lon = 0.0
            MainActivity.path!!.forEach {
                lat = it.lat!!
                lon = it.lon!!
                polylineOptions.add(LatLng(lat, lon))
                if (count == 0 ){
                    val startLatLng = LatLng(lat, lon)
                    val zoomLevel = 15f

                    googleMap.addMarker(MarkerOptions().position(startLatLng).title("Начало пути"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, zoomLevel))
                }
                count++
            }
            googleMap.addMarker(MarkerOptions().position(LatLng(lat, lon)).title("Конец пути"))
            googleMap.addPolyline(polylineOptions)
            MainActivity.path = null
        }
    }

    override fun onResume() {
        super.onResume()

        binding.map.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        binding.map.onLowMemory()
    }

    override fun onPause() {
        super.onPause()

        binding.map.onPause()
    }
}
