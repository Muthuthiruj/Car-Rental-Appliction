package com.example.carrental.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.carrental.R
import com.example.carrental.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayout

class AuthFragment : Fragment(), AuthTabSwitcher {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = childFragmentManager
        val tabLayout: TabLayout = binding.tabLayout

        // Dynamically add tabs
        tabLayout.addTab(tabLayout.newTab().setText("Sign In"))
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"))


        // Load the default fragment (SignInFragment)
        replaceFragment(SignInFragment(), animate = false)

        // Listen for tab selection changes
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.position) {
                    0 -> SignInFragment()
                    else -> SignUpFragment()
                }
                replaceFragment(fragment, animate = true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }


    private fun replaceFragment(fragment: Fragment, animate: Boolean) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        if (animate) {
            transaction.setCustomAnimations(
                R.anim.slide_in_right,  // Enter animation
                R.anim.slide_out_left,  // Exit animation
                R.anim.slide_in_left,   // Pop enter animation (when navigating back)
                R.anim.slide_out_right  // Pop exit animation (when navigating back)
            )
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commitAllowingStateLoss()
    }


    override fun switchToTab(tabIndex: Int) {
        binding.tabLayout.getTabAt(tabIndex)?.select()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
