package com.example.carrental.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrental.R
import com.example.carrental.model.RentalPlan

class RentalPlanViewModel : ViewModel() {
    private val _rentalPlans = MutableLiveData<List<RentalPlan>>()
    val rentalPlans: LiveData<List<RentalPlan>> = _rentalPlans

    init {
        loadRentalPlans()
    }

    fun loadRentalPlans() {
        // Simulate loading rental plans from a data source
        val initialPlans = listOf(
            RentalPlan(
                iconResId = R.drawable.clock,
                title = "Hourly Rental",
                price = "$15/hour",
                description = "Perfect for quick trips around town",
                features = listOf("Includes basic insurance", "Free cancellation", "50 free km/hour")
            ),
            RentalPlan(
                iconResId = R.drawable.time,
                title = "Daily Rental",
                price = "$120/day",
                description = "Best for full day adventures",
                features = listOf("Includes premium insurance", "Free cancellation", "200 free km/day")
            ),
            RentalPlan(
                iconResId = R.drawable.calendar1,
                title = "Weekly Rental",
                price = "$600/week",
                description = "Best value for extended trips",
                features = listOf("Includes premium insurance", "Free cancellation", "Unlimited mileage")
            )
            // Add more plans as needed
        )
        _rentalPlans.value = initialPlans
    }

    fun selectPlan(plan: RentalPlan) {
        val currentPlans = _rentalPlans.value?.toMutableList() ?: mutableListOf()
        val updatedPlans = currentPlans.map {
            if (it.title == plan.title) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
        _rentalPlans.value = updatedPlans
    }
}