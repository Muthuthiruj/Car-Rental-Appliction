package com.example.carrental.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.carrental.R
import com.example.carrental.databinding.FragmentSplashBookingitemBinding
import com.example.carrental.model.Car

class SplashBookingItemFragment : Fragment() {
    private lateinit var binding: FragmentSplashBookingitemBinding
    var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBookingitemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedCar: Car? = arguments?.getParcelable("selectedCar")



        selectedCar?.let {
            binding.carNameTextView.text = it.name
            binding.carImageView.setImageResource(it.image)
            binding.carPrice.text = "${it.price}" // Format price with a dollar sign
//            binding.carCategory.text = it.category
            binding.accelerationValue.text = " ${it.acceleration}"
            binding.rangeValue.text= " ${it.range}"
            binding.seatsValue.text= "${it.seats}"
            binding.cameraValue.text  = "${it.camerasAndSensors}"
            binding.batteryValue.text = "${it.battery}"
            binding.autopilotValue.text = "${it.autopilot}"
            binding.carview1.setImageResource((it.aboutimage))
        }

        // Toggle visibility for Car Details on click

//        binding.ivcardetails.setOnClickListener {
//            isExpanded = !isExpanded  // Toggle state
//
//            binding.descriptionlayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
//            binding.ivcardetails.rotation = if (isExpanded) -270f else 270f
//
//            val message = if (isExpanded) "Car details shown" else "Car details hidden"
//            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        }

        binding.ivcardetails.setOnClickListener {
            isExpanded = !isExpanded  // Toggle state

            if (isExpanded) {
                binding.descriptionlayout.visibility = View.VISIBLE
                binding.descriptionlayout.alpha = 0f
                binding.descriptionlayout.animate().alpha(1f).setDuration(300).start()
                binding.ivcardetails.animate().rotation(90f).setDuration(300).start()
            } else {
                binding.descriptionlayout.animate().alpha(0f).setDuration(200).withEndAction {
                    binding.descriptionlayout.visibility = View.GONE
                }.start()
                binding.ivcardetails.animate().rotation(270f).setDuration(300).start()
            }

            val message = if (isExpanded) "Car details shown" else "Car details hidden"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }



        // Initialize startLocation after view is created
        val startLocation: EditText = view.findViewById(R.id.startLocation)

        // Make it non-editable
        startLocation.isFocusable = false
        startLocation.isFocusableInTouchMode = false
        startLocation.isClickable = true

        // Navigate to MapFragment when clicked
        startLocation.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_bookingItemFragment_to_mapFragment)
        }
    }

}
