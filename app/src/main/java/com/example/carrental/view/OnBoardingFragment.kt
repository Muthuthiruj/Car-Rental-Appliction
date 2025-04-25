package com.example.carrental.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.carrental.viewModel.OnBoardingViewModel
import com.example.carrental.R
import com.example.carrental.adapter.OnboardingAdapter
import com.example.carrental.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OnboardingAdapter

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe ViewModel to get onboarding items
        viewModel.onBoardingItem.observe(viewLifecycleOwner) { items ->
            adapter = OnboardingAdapter(items)
            binding.viewPager.adapter = adapter

            // Set onboarding indicators after adapter is initialized
            setOnboardingIndicator(adapter.itemCount)
            setCurrentOnboardingIndicators(0)

            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentOnboardingIndicators(position)

                    // Show/hide buttons based on last page
                    if (position == adapter.itemCount - 1) {
                        binding.nextButton.visibility = View.VISIBLE
                        binding.skipButton.visibility = View.GONE
                    } else {
                        binding.nextButton.visibility = View.GONE
                        binding.skipButton.visibility = View.VISIBLE
                    }
                }
            })
        }

        // Set nextButton click listener
        binding.nextButton.setOnClickListener {
            if (binding.viewPager.currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem += 1
            } else {
                goToMain()
            }
        }

        // Set skipButton click listener
        binding.skipButton.setOnClickListener {
            goToMain()
        }

        // Store onboarding completion in SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("Onboarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Shown", true)
            apply()
        }
    }

    private fun setOnboardingIndicator(itemCount: Int) {
        val indicators = arrayOfNulls<ImageView>(itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(8, 0, 8, 0) }

        binding.onbindicator.removeAllViews()

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext()).apply {
                setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.default_indicator))
                this.layoutParams = layoutParams
            }
            binding.onbindicator.addView(indicators[i])
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = binding.onbindicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.onbindicator.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.selected_dot))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.default_dot))
            }
        }
    }

    private fun goToMain() {
        findNavController().navigate(R.id.action_onBoardingFragment_to_authFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
