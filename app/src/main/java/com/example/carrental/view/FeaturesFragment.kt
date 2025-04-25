package com.example.carrental.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carrental.R
import com.example.carrental.databinding.FragmentFeaturesBinding
import com.example.carrental.model.Car


class FeaturesFragment : Fragment() {
    private var _binding: FragmentFeaturesBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedCar: Car? = arguments?.getParcelable("selectedCar")
        selectedCar?.let {
            binding.accelerationValue.text = " ${it.acceleration}"
            binding.rangeValue.text= " ${it.range}"
            binding.seatsValue.text= "${it.seats}"
            binding.cameraValue.text  = "${it.camerasAndSensors}"
            binding.batteryValue.text = "${it.battery}"
            binding.autopilotValue.text = "${it.autopilot}"
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_features, container, false)
    }


}