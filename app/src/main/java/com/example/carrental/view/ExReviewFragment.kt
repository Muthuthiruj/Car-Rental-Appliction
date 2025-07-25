package com.example.carrental.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.adapter.ExReviewAdapter
import com.example.carrental.databinding.FragmentExReviewBinding
import com.example.carrental.viewModel.ApiViewModel
import com.example.carrental.viewModel.ReviewData

class ExReviewFragment : Fragment() {

    private var  _binding: FragmentExReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ApiViewModel
    private lateinit var adapter: ExReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExReviewBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        binding?.rvReviews?.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExReviewAdapter(emptyList()) {
            Log.d("ExReviewFragment", "Like clicked")

        }
        binding?.rvReviews?.adapter = adapter

        // Observe the LiveData from the ViewModel
        viewModel.liveReviewData.observe(viewLifecycleOwner) { reviewItems ->
            // Update the adapter with the new data
            val reviewDataList = reviewItems.map { reviewCarItem ->
                ReviewData(
                    userName = reviewCarItem.userName,
                    reviewText = reviewCarItem.reviewText,
                    rating = reviewCarItem.rating,
                    date = reviewCarItem.date,
                    carModel = reviewCarItem.carModel,
                    rentalPeriod = reviewCarItem.rentalPeriod,
                    likeCount = reviewCarItem.likeCount,
                    userAvatar = reviewCarItem.userAvatar
                )
            }
            adapter.updateData(reviewDataList)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


