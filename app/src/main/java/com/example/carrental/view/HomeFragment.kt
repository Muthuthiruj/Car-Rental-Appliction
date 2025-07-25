package com.example.carrental.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.carrental.R
import com.example.carrental.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val imageSlider: ImageSlider = binding.imageSlider
        val imageList = arrayListOf(
            SlideModel(R.drawable.deal1, "Luxury Car Rental"),
            SlideModel(R.drawable.deal2, "Exclusive Discounts"),
            SlideModel(R.drawable.deal3, "Drive in Style"),
            SlideModel(R.drawable.deal1, "Best Prices Guaranteed")
        )
        binding.clAutoRefill.setOnClickListener {
            findNavController().navigate(R.id.trendingCarfragment)
        }

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
