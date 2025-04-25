package com.example.carrental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.databinding.Banner1LayoutBinding
import com.example.carrental.model.BannerItem

class BannerAdapter(private val bannerList: List<BannerItem>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding =
            Banner1LayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int {
        return bannerList.size
    }

    class BannerViewHolder(private val binding: Banner1LayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(bannerItem: BannerItem) {
            binding.bannerImage.setImageResource(bannerItem.imageResId)
            binding.tvOffer.text = bannerItem.title
            binding.tvOfferField.text = bannerItem.description
        }
    }
}