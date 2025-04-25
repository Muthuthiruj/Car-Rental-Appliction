package com.example.carrental.model


data class RentalPlan(
    val title: String,
    val price: String,
    val description: String,
    val features: List<String>,
    val iconResId: Int,
    val isSelected: Boolean = false
)