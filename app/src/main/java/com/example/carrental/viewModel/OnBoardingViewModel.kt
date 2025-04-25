package com.example.carrental.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrental.R
import com.example.carrental.model.OnBoardingItem


class OnBoardingViewModel: ViewModel() {

    private val _onBoardingItem = MutableLiveData<List<OnBoardingItem>>()
    val onBoardingItem: LiveData<List<OnBoardingItem>> get() = _onBoardingItem

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> get() = _currentPage

    init {
        _onBoardingItem.value = listOf(
            OnBoardingItem(
                title = "Drive Your Dream - Premium Car Rental Services",
                imageResId = R.drawable.car4
            ),
            OnBoardingItem(
                title = "Ride Your Way – Cars for Every Journey",
                imageResId = R.drawable.car5
            ),
            OnBoardingItem(
                title = "24/7 customer support and roadside assistance",
                imageResId = R.drawable.car6
            )
        )
        _currentPage.value = 0
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }
}