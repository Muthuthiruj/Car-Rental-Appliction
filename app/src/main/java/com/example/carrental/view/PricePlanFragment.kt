package com.example.carrental.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.carrental.adapter.RentalPlanAdapter
import com.example.carrental.databinding.FragmentPricePlanBinding

import com.example.carrental.viewmodel.RentalPlanViewModel

class PricePlanFragment : Fragment() {

    private var _binding: FragmentPricePlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var rentalPlanViewModel: RentalPlanViewModel
    private lateinit var rentalPlanAdapter: RentalPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPricePlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        rentalPlanViewModel = ViewModelProvider(this).get(RentalPlanViewModel::class.java)

        // Set up RecyclerView
        binding.rvRentalPlans.layoutManager = LinearLayoutManager(requireContext())

        // Observe the rental plans LiveData
        rentalPlanViewModel.rentalPlans.observe(viewLifecycleOwner) { plans ->
            rentalPlanAdapter = RentalPlanAdapter(plans) { selectedPlan ->
                rentalPlanViewModel.selectPlan(selectedPlan)
                // No need to manually notifyDataSetChanged here if selectPlan updates LiveData
            }
            binding.rvRentalPlans.adapter = rentalPlanAdapter
        }

        // Load the rental plans (if not already loaded)
        if (rentalPlanViewModel.rentalPlans.value.isNullOrEmpty()) {
            rentalPlanViewModel.loadRentalPlans()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}