package com.example.carrental.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.carrental.R
import com.example.carrental.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the default fragment
        loadFragment(HomeFragment())

        // Handle bottom navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_browser -> loadFragment(BrowseFragment())
                R.id.nav_booking -> loadFragment(BookingFragment())
                R.id.nav_settings -> loadFragment(SettingFragment())
                else -> false
            }
            true
        }
    }

    // Function to replace the fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container1, fragment)
            .commit()
    }
}
