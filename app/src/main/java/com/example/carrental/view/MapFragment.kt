package com.example.carrental.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.carrental.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class MapFragment : Fragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private lateinit var placesClient: PlacesClient
    private lateinit var etSearchLocation: AutoCompleteTextView
    private lateinit var adapter: PlacesAutoCompleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize Places API
        Places.initialize(requireContext(), "YOUR_GOOGLE_API_KEY_HERE")
        placesClient = Places.createClient(requireContext())

        // Initialize Search Bar
        etSearchLocation = view.findViewById(R.id.etSearchLocation)

        // Load Google Map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        // Setup Autocomplete Search
        setupAutoCompleteSearch()

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val defaultLocation = LatLng(37.7749, -122.4194) // San Francisco default
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
    }

    private fun setupAutoCompleteSearch() {
        adapter = PlacesAutoCompleteAdapter(requireContext(), placesClient)
        etSearchLocation.setAdapter(adapter)

        etSearchLocation.setOnItemClickListener { parent, _, position, _ ->
            val selectedPlace = parent.getItemAtPosition(position) as AutocompletePrediction
            fetchPlaceDetailsAndMoveCamera(selectedPlace.placeId)
        }
    }

    private fun fetchPlaceDetailsAndMoveCamera(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val location = place.latLng
                if (location != null) {
                    googleMap?.clear()
                    googleMap?.addMarker(MarkerOptions().position(location).title(place.name))
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
    }
}
