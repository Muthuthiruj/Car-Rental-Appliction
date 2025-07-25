package com.example.carrental.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.carrental.R
import com.example.carrental.adapter.AuthPagerAdapter
import com.example.carrental.databinding.FragmentAuthBinding

interface AuthTabSwitcher {
    fun switchToTab(tabIndex: Int)
}

interface AuthNavigator {
    fun navigateToHome()
}
class AuthFragment : Fragment(), AuthTabSwitcher ,AuthNavigator{

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        val tabSignIn = binding.tabSignIn
        val tabSignUp = binding.tabSignUp

        val adapter = AuthPagerAdapter(childFragmentManager)
        viewPager.adapter = adapter

        updateTabAppearance(0)

        tabSignIn.setOnClickListener {
            viewPager.currentItem = 0
        }

        tabSignUp.setOnClickListener {
            viewPager.currentItem = 1
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Not needed for this implementation
            }

            override fun onPageSelected(position: Int) {
                updateTabAppearance(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })
    }

    override fun switchToTab(tabIndex: Int) {
        binding.viewPager.currentItem = tabIndex
    }

    override fun navigateToHome() {
        findNavController().navigate(R.id.action_authFragment_to_homeFragment)
    }
    private fun updateTabAppearance(selectedPosition: Int) {
        val tabSignIn = binding.tabSignIn
        val tabSignUp = binding.tabSignUp
        val context = requireContext()

        when (selectedPosition) {
            0 -> {
                // "Sign In" tab is selected
                tabSignIn.setBackgroundResource(R.drawable.tab_selected_background)
                tabSignIn.setTextColor(ContextCompat.getColor(context, R.color.white))
                tabSignIn.setTextSize(16f) // Set the text size
                tabSignIn.setTypeface(tabSignIn.typeface, android.graphics.Typeface.BOLD) // Set bold style

                // "Sign Up" tab is unselected
                tabSignUp.setBackgroundResource(android.R.color.transparent)
                tabSignUp.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                tabSignUp.setTextSize(16f) // Set the text size
                tabSignUp.setTypeface(tabSignUp.typeface, android.graphics.Typeface.NORMAL) // Set normal style
            }
            1 -> {
                // "Sign In" tab is unselected
                tabSignIn.setBackgroundResource(android.R.color.transparent)
                tabSignIn.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                tabSignIn.setTextSize(16f)
                tabSignIn.setTypeface(tabSignIn.typeface, android.graphics.Typeface.NORMAL)

                // "Sign Up" tab is selected
                tabSignUp.setBackgroundResource(R.drawable.tab_selected_background)
                tabSignUp.setTextColor(ContextCompat.getColor(context, R.color.white))
                tabSignUp.setTextSize(16f)
                tabSignUp.setTypeface(tabSignUp.typeface, android.graphics.Typeface.BOLD)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}