package com.example.carrental.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.carrental.R
import com.example.carrental.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.white, null)
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)


        try {

            // Handle back press
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.onBoardingFragment) {
                        finish()
                    } else {
                        // Otherwise, navigate up
                        navController.navigateUp()
                    }
                }
            })

        }catch (e:Exception){
            Log.d("Navigation","App closed ${e.message}")

        }

    }
}