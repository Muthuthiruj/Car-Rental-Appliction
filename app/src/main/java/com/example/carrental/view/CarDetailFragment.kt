package com.example.carrental.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.carrental.R
import com.example.carrental.databinding.FragmentCarDetailBinding
import com.example.carrental.model.Car
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import kotlin.math.abs

class CarDetailFragment : Fragment() {

    private var _binding: FragmentCarDetailBinding? = null
    private val binding get() = _binding!!
    var isExpanded = false
    private var currentFragment: Fragment? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)

        /*setupToolbar()
        setupAppBarBehavior()*/
        // Set up the bottom sheet behavior
        try {
            bottomSheetBehavior = BottomSheetBehavior.from(binding.contentSheet.root)
        }
        catch (e:Exception){
            Log.d("checkbottomsheet","bottomsheet catch -${e.message}")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Measure the description layout initially
        binding.descriptionlayout.post {
            binding.descriptionlayout.measure(
                View.MeasureSpec.makeMeasureSpec(binding.root.width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            // Set initial collapsed state
            binding.descriptionlayout.layoutParams.height = 0
            binding.descriptionlayout.visibility = View.GONE
        }

        // Setup tabs with equal width and text size
        binding.tabLayout.apply {


            // Remove default elevation if needed
            elevation = 0f

            // Add tabs
            addTab(newTab().setText("Features"))
            addTab(newTab().setText("Price Plan"))
            addTab(newTab().setText("Reviews"))

            // Ensure equal distribution
            post {
                val tabCount = tabCount
                if (tabCount > 0) {
                    val tabMinWidth = width / tabCount
                    for (i in 0 until tabCount) {
                        getTabAt(i)?.view?.minimumWidth = tabMinWidth
                    }
                }
            }
            val selectedCar: Car? = arguments?.getParcelable("selectedCar")
            selectedCar?.let {
                binding.carNameTextView.text = it.name
                binding.carImageView.setImageResource(it.image)
                binding.carPrice.text = "${it.price}" // Format price with a dollar sign
//            binding.carCategory.text = it.category

                binding.carview1.setImageResource((it.aboutimage))
            }

            // Add selection listener
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    // Optional: Add animation to selected tab
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

        // Set up the bottom sheet behavior
        try {
            // Set swipe listener
            binding.swipeBtn.setOnStateChangeListener { isActive ->
                if (isActive) {
                    // When swiped (active) - hide lock icon
                    binding.lockBtn.visibility = View.GONE
                    binding.lottieBook.visibility = View.GONE

                    /*bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    bottomSheetBehavior.peekHeight = 400*/


                } else {
                    // When not swiped - show lock icon
                    binding.lockBtn.visibility = View.VISIBLE
                    binding.lottieBook.visibility = View.VISIBLE


/*
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
*/

                }
            }
        }
        catch (e:Exception){
            Log.d("checkbottomsheet","bottomsheet catch 1 -${e.message}")
        }
        // Set swipe listener
        binding.swipeBtn.setOnStateChangeListener { isActive ->
            if (isActive) {
                // When swiped (active) - hide lock icon
                binding.lockBtn.visibility = View.GONE
                binding.lottieBook.visibility = View.GONE

               // bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED


            } else {
                // When not swiped - show lock icon
                binding.lockBtn.visibility = View.VISIBLE
                binding.lottieBook.visibility = View.VISIBLE

                //bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            }
        }

        binding.ivcardetails.setOnClickListener {
            isExpanded = !isExpanded  // Toggle state
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

        // Load default fragment
        loadFragment(FeaturesFragment())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> loadFragment(FeaturesFragment())
                    1 -> loadFragment(PricePlanFragment())
                    2 -> loadFragment(ReviewsFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }



    private fun expandDescription() {
        // Use the already measured width
        val widthSpec = View.MeasureSpec.makeMeasureSpec(binding.descriptionlayout.measuredWidth, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        binding.descriptionlayout.measure(widthSpec, heightSpec)

        val targetHeight = binding.descriptionlayout.measuredHeight

        binding.descriptionlayout.visibility = View.VISIBLE
        binding.descriptionlayout.alpha = 0f
        binding.descriptionlayout.layoutParams.height = 0
        binding.descriptionlayout.requestLayout()

        ValueAnimator.ofInt(0, targetHeight).apply {
            duration = 300
            addUpdateListener { animation ->
                binding.descriptionlayout.layoutParams.height = animation.animatedValue as Int
                binding.descriptionlayout.requestLayout()
                // Calculate alpha based on animation progress
                binding.descriptionlayout.alpha = animation.animatedFraction
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.descriptionlayout.alpha = 1f
                    // Optional: Keep it wrap_content after animation
                    binding.descriptionlayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    binding.descriptionlayout.requestLayout()
                }
            })
            start()
        }
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupAppBarBehavior() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // Calculate scroll percentage (0 = expanded, 1 = collapsed)
            val scrollRange = appBarLayout.totalScrollRange.toFloat()
            val percentage = abs(verticalOffset) / scrollRange

            // Optional: Fade out header elements as it collapses
            binding.collapsingToolbar.alpha = 1 - percentage
        })
    }
    private fun collapseDescription() {
        val initialHeight = binding.descriptionlayout.measuredHeight

        ValueAnimator.ofInt(initialHeight, 0).apply {
            duration = 300
            addUpdateListener { animation ->
                binding.descriptionlayout.layoutParams.height = animation.animatedValue as Int
                binding.descriptionlayout.requestLayout()
                // Fade out while collapsing
                binding.descriptionlayout.alpha = 1 - animation.animatedFraction
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.descriptionlayout.visibility = View.GONE
                    binding.descriptionlayout.alpha = 0f
                    // Reset height
                    binding.descriptionlayout.layoutParams.height = 0
                }
            })
            start()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        currentFragment = fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.containter1, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}