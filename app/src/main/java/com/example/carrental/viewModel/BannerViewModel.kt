package com.example.carrental.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrental.R
import com.example.carrental.model.BannerItem

class BannerViewModel : ViewModel() {

    private val _bannerList = MutableLiveData<List<BannerItem>>()
    val bannerList: LiveData<List<BannerItem>> get() = _bannerList

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> get() = _currentPage

    init {
        _bannerList.value = listOf(
            BannerItem(
                imageResId = R.drawable.banner_img1,
                title = "Limited Time Offer!",
                description = "Get 20% Off on Your First Booking"
            ),
            BannerItem(
                imageResId = R.drawable.banner_img1,
                title = "Luxury at Your Fingertips",
                description = "Book Premium Cars with Exclusive Discounts"
            ),
            BannerItem(
                imageResId = R.drawable.banner_img1,
                title = "Drive in Style",
                description = "Special Offers on Sports & SUVs!"
            )
        )
        _currentPage.value = 0
    }

    fun setCurrentPage(page: Int) {
        _currentPage.value = page
    }
}
