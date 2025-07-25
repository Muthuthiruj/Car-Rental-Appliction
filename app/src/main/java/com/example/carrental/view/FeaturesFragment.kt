package com.example.carrental.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carrental.databinding.FragmentFeaturesBinding
import com.example.carrental.model.Car

class FeaturesFragment : Fragment() {
    private var _binding: FragmentFeaturesBinding? = null
    private val binding get() = _binding!!
    private var selectedCar: Car? = null  // Changed to nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            selectedCar = arguments?.getParcelable("selected_car")
            if (selectedCar == null) {
                Log.e("FeaturesFragment", "No car data provided in arguments")
                // Optionally navigate back or show error
            }
        } catch (e: Exception) {
            Log.e("FeaturesFragment", "Error parsing car data: ${e.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCar?.let { car ->  // Safe access with let
            with(binding) {
                accelerationValue.text = car.acceleration
                rangeValue.text = car.range
                seatsValue.text = car.seats
                cameraValue.text = car.camerasAndSensors
                batteryValue.text = car.battery
                autopilotValue.text = car.autopilot
            }
        } ?: run {
            Log.e("FeaturesFragment", "Selected car is null")
            // Handle null case - show error message or navigate back
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}