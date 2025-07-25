package com.example.carrental.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carrental.R
import com.example.carrental.databinding.FragmentCarDetailBinding
import com.example.carrental.model.Car
import com.google.android.material.appbar.AppBarLayout

import com.google.android.material.tabs.TabLayout
import kotlin.math.abs

class CarDetailFragment : Fragment() {

    private var _binding: FragmentCarDetailBinding? = null
    private val binding get() = _binding!!
    private var isExpanded = false
    private var selectedCar: Car? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve car data safely
        selectedCar = arguments?.getParcelable("selected_car")

        if (selectedCar == null) {
            Log.e("CarDataCheck", "Received null car in destination fragment")
            Toast.makeText(requireContext(), "Car data not available", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // Initialize UI with car data
        initializeCarData()

        // Setup description toggle
        setupDescriptionToggle()

        // Setup tabs
        setupTabs()

        // Setup swipe button
        setupSwipeButton()

        // Load default fragment
        loadFragment(FeaturesFragment())
    }

    private fun initializeCarData() {
        selectedCar?.let { car ->
            try {
                binding.carNameTextView.text = car.name
                binding.carImageView.setImageResource(car.image)
                binding.carPrice.text = "$${car.price}"
                binding.carview1.setImageResource(car.aboutimage)
            } catch (e: Exception) {
                Log.e("CarDataCheck", "Error setting car data: ${e.message}")
            }
        }
    }

    private fun setupDescriptionToggle() {
        binding.descriptionlayout.post {
            binding.descriptionlayout.measure(
                View.MeasureSpec.makeMeasureSpec(binding.root.width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            binding.descriptionlayout.layoutParams.height = 0
            binding.descriptionlayout.visibility = View.GONE
        }

        binding.ivcardetails.setOnClickListener {
            isExpanded = !isExpanded
            binding.descriptionlayout.layoutParams.height =
                ViewGroup.LayoutParams.WRAP_CONTENT

            if (isExpanded) {
                expandDescription()
                binding.ivcardetails.animate().rotation(90f).setDuration(300).start()
            } else {
                collapseDescription()
                binding.ivcardetails.animate().rotation(270f).setDuration(300).start()
            }

            val message = if (isExpanded) "Car details shown" else "Car details hidden"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTabs() {
        binding.tabLayout.apply {
            elevation = 0f

            addTab(newTab().setText("Features"))
            addTab(newTab().setText("Price Plan"))
            addTab(newTab().setText("Reviews"))

            post {
                val tabCount = tabCount
                if (tabCount > 0) {
                    val tabMinWidth = width / tabCount
                    for (i in 0 until tabCount) {
                        getTabAt(i)?.view?.minimumWidth = tabMinWidth
                    }
                }
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.view?.animate()?.scaleX(1.05f)?.scaleY(1.05f)?.setDuration(200)?.start()
                    when (tab.position) {
                        0 -> loadFragment(FeaturesFragment())
                        1 -> loadFragment(PricePlanFragment())
                        2 -> loadFragment(ReviewsFragment())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.view?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)?.start()
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    private fun setupSwipeButton() {
        binding.swipeBtn.setOnStateChangeListener { isActive ->
            if (isActive) {
                binding.lockBtn.visibility = View.GONE
                binding.lottieBook.visibility = View.GONE
                findNavController().navigate(R.id.action_carDetailFragment_to_bookingFormFragment)
            } else {
                binding.lockBtn.visibility = View.VISIBLE
                binding.lottieBook.visibility = View.VISIBLE
            }
        }
    }

    private fun expandDescription() {
        binding.descriptionlayout.visibility = View.VISIBLE
        binding.descriptionlayout.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    private fun collapseDescription() {
        binding.descriptionlayout.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                binding.descriptionlayout.visibility = View.GONE
            }
            .start()
    }

    private fun loadFragment(fragment: Fragment) {
        // Pass car data to the fragment
        fragment.arguments = Bundle().apply {
            putParcelable("selected_car", selectedCar)
        }

        childFragmentManager.beginTransaction()
            .replace(binding.containter1.id, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}