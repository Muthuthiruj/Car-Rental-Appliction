package com.example.carrental.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.R
import com.example.carrental.adapter.CarAdapter
import com.example.carrental.adapter.CarLogoAdapter
import com.example.carrental.adapter.FilterAdapter
import com.example.carrental.databinding.FragmentTrendingCarfragmentBinding
import com.example.carrental.model.Car
import java.util.Arrays

class TrendingCarfragment : Fragment() {
    private var binding: FragmentTrendingCarfragmentBinding? = null
    private var filterAdapter: FilterAdapter? = null
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrendingCarfragmentBinding.inflate(inflater, container, false)

        // Initialize RecyclerViews
        setupCarLogoRecyclerView()
        startAutoScroll()
        setupFilterRecyclerView()
        setupAvailableCarRecyclerView()

        return binding!!.root
    }

    private fun setupCarLogoRecyclerView() {
        val carLogos = Arrays.asList(
            R.drawable.car13, R.drawable.car11, R.drawable.car12, R.drawable.car10,
            R.drawable.car14, R.drawable.car15, R.drawable.car16, R.drawable.car17,
            R.drawable.car18, R.drawable.car19
        )

        val adapter = CarLogoAdapter(carLogos)
        binding!!.trendingBrandsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.trendingBrandsRecyclerView.adapter = adapter
    }

    private fun startAutoScroll() {
        if (binding!!.trendingBrandsRecyclerView.adapter == null) return

        val runnable: Runnable = object : Runnable {
            override fun run() {
                val itemCount = binding!!.trendingBrandsRecyclerView.adapter!!.itemCount
                if (itemCount == 0) return

                currentPosition++
                if (currentPosition >= itemCount) {
                    currentPosition = 0 // Loop back to the first item
                }

                binding!!.trendingBrandsRecyclerView.smoothScrollToPosition(currentPosition)
                handler.postDelayed(this, 2000)
            }
        }

        handler.postDelayed(runnable, 2000)
    }

    private fun setupFilterRecyclerView() {
        // Initialize filter options
        val filterOptions: List<String> = mutableListOf(
            "All",
            "Classic",
            "Modern",
            "Luxury",
            "SUV",
            "Recommended",
            "Regular Booking"
        )

        // Initialize adapter
        filterAdapter = FilterAdapter(filterOptions) { filter: String ->
            Toast.makeText(
                requireContext(),
                "Selected: $filter", Toast.LENGTH_SHORT
            ).show()
        }

        // Set layout manager and adapter
        binding!!.filterRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.filterRecyclerView.adapter = filterAdapter
        binding!!.filterRecyclerView.clipToPadding = false
        binding!!.filterRecyclerView.clipChildren = false
        binding!!.filterRecyclerView.scrollToPosition(0)

        // Set FAB click listener
//        binding!!.fabFilter.setOnClickListener { view ->
//            Toast.makeText(
//                requireContext(),
//                "Filter options",
//                Toast.LENGTH_SHORT
//            ).show()
//        }

        binding!!.filterRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding!!.filterRecyclerView.adapter = filterAdapter
        binding!!.filterRecyclerView.clipToPadding = false
        binding!!.filterRecyclerView.clipChildren = false
        binding!!.filterRecyclerView.scrollToPosition(0)


    }

    private fun setupAvailableCarRecyclerView() {
        binding!!.availablecarRecycler.layoutManager = LinearLayoutManager(requireContext())

        val carList = Arrays.asList(
            Car("Chevrolet Corvette", "Classic - C2", 570, R.drawable.display1, "4.3s", "400 km", "2", "Rearview Camera", "60 kWh", "No",R.drawable.display1),
            Car("Ford Mustang", "Muscle - GT", 620, R.drawable.display2, "4.1s", "450 km", "4", "360° Sensors", "75 kWh", "Yes",R.drawable.display2),
            Car("Dodge Challenger", "Muscle - SRT", 650, R.drawable.display3, "3.9s", "500 km", "4", "Parking Assist", "80 kWh", "Yes",R.drawable.display3),
            Car("Tesla Model S", "Electric - Sedan", 750, R.drawable.display4, "2.9s", "600 km", "5", "Autopilot", "100 kWh", "Yes",R.drawable.display4),
            Car("Porsche 911", "Sports - Turbo", 800, R.drawable.display6, "3.5s", "550 km", "2", "Night Vision", "85 kWh", "Yes",R.drawable.display6),
            Car("BMW M3", "Luxury - Performance", 690, R.drawable.display7, "3.8s", "480 km", "5", "Blind Spot Monitoring", "78 kWh", "Yes",R.drawable.display7),
            Car("Mercedes AMG GT", "Sports - Coupe", 820, R.drawable.display8, "3.4s", "500 km", "2", "Lane Assist", "88 kWh", "Yes",R.drawable.display8),
            Car("Audi R8", "Supercar - V10", 900, R.drawable.display9, "3.2s", "530 km", "2", "360° Sensors", "92 kWh", "Yes",R.drawable.display9),
            Car("Lamborghini Huracán", "Supercar - EVO", 1100, R.drawable.display10, "2.8s", "490 km", "2", "Rear Camera", "95 kWh", "Yes",R.drawable.display10),
            Car("Ferrari 488", "Supercar - Spider", 1200, R.drawable.display12, "3.0s", "470 km", "2", "Adaptive Cruise", "98 kWh", "Yes",R.drawable.display12),
            Car("Aston Martin DB11", "Grand Tourer", 950, R.drawable.display13, "3.7s", "510 km", "2", "Front & Rear Sensors", "89 kWh", "Yes",R.drawable.display13),
            Car("McLaren 720S", "Supercar - Ultimate", 1250, R.drawable.display14, "2.7s", "480 km", "2", "Traffic Sign Recognition", "100 kWh", "Yes",R.drawable.display14),
            Car("Bugatti Chiron", "Hypercar - Sport", 2500, R.drawable.display15, "2.4s", "460 km", "2", "Advanced Driver Assist", "120 kWh", "Yes",R.drawable.display15),
            Car("Koenigsegg Jesko", "Hypercar - Track", 3000, R.drawable.display16, "2.5s", "440 km", "2", "Laser Radar Sensors", "130 kWh", "Yes",R.drawable.display16),
            Car("Rolls-Royce Phantom", "Luxury - Sedan", 1800, R.drawable.display17, "4.5s", "520 km", "4", "Self-Closing Doors", "110 kWh", "Yes",R.drawable.display17),
            Car("Maserati MC20", "Sports - Elite", 1050, R.drawable.display11, "2.9s", "470 km", "2", "Heads-up Display", "95 kWh", "Yes",R.drawable.display11),
            Car("Lexus LC 500", "Luxury - Coupe", 780, R.drawable.display1, "4.4s", "490 km", "4", "Road Sign Assist", "86 kWh", "Yes",R.drawable.display1),
            Car("Jaguar F-Type", "Luxury - Sport", 720, R.drawable.display4, "3.9s", "500 km", "2", "360° View Camera", "82 kWh", "Yes",R.drawable.display4),
            Car("Nissan GT-R", "Performance - Nismo", 950, R.drawable.display1, "3.1s", "480 km", "4", "Active Noise Cancellation", "96 kWh", "Yes",R.drawable.display1),
            Car("Chevrolet Camaro", "Muscle - SS", 680, R.drawable.display4, "4.0s", "450 km", "4", "Blind Spot Warning", "80 kWh", "Yes",R.drawable.display4)
        )
        val adapter = CarAdapter(requireContext(), carList)
        binding!!.availablecarRecycler.adapter = adapter
        binding!!.availablecarRecycler.scheduleLayoutAnimation();

        adapter.onItemClick = { car ->
            val bundle = Bundle().apply {
                putParcelable("selectedCar", car)
            }
            findNavController().navigate(R.id.carDetailFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop auto-scroll to prevent memory leaks
        binding = null
    }
}