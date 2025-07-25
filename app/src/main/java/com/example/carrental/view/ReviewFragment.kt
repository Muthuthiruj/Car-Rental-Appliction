package com.example.carrental.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.adapter.ReviewAdapter
import com.example.carrental.databinding.FragmentReviewBinding
import com.example.carrental.viewModel.ReviewViewModel

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: ReviewViewModel
    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        // Setup RecyclerView
        adapter = ReviewAdapter(emptyList()) { reviewId ->
            viewModel.toggleLike(reviewId)
        }

        binding.rvReviews.layoutManager = LinearLayoutManager(context)
        binding.rvReviews.adapter = adapter

        // Observe LiveData
        viewModel.reviewList.observe(viewLifecycleOwner) { reviews ->
            adapter.updateData(reviews)
        }

        viewModel.averageRating.observe(viewLifecycleOwner) { rating ->
            binding.rbAverage.rating = rating
            binding.tvAverageRating.text = "%.1f".format(rating)
        }

        viewModel.totalReviews.observe(viewLifecycleOwner) { count ->
            binding.tvReviewCount.text =
                "Based on $count ${if (count == 1) "review" else "reviews"}"
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
