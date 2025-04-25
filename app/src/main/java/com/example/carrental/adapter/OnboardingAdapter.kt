package com.example.carrental.adapter


import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carrental.model.OnBoardingItem
import com.example.carrental.databinding.OnboardingScreenBinding



class OnboardingAdapter( private val onBoardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = OnboardingScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val onBoarding = onBoardingItems[position]
        holder.bind(onBoarding)
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }

    inner class OnboardingViewHolder(private val binding: OnboardingScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onBoardingItem: OnBoardingItem) {
            Glide.with(binding.root.context).load(onBoardingItem.imageResId).into(binding.imageonboarding)
            binding.caption.text = onBoardingItem.title

        }
    }
}