package com.example.carrental.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carrental.view.FeaturesFragment
import com.example.carrental.view.PricePlanFragment
import com.example.carrental.view.ReviewsFragment

class CarDetailsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FeaturesFragment()
            1 -> PricePlanFragment()
            2 -> ReviewsFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}