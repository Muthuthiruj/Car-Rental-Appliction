package com.example.carrental.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrental.R
import com.example.carrental.model.Review
import java.util.Date

class ReviewViewModel : ViewModel() {

    private val _reviewList = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> get() = _reviewList

    private val _averageRating = MutableLiveData<Float>()
    val averageRating: LiveData<Float> get() = _averageRating

    private val _totalReviews = MutableLiveData<Int>()
    val totalReviews: LiveData<Int> get() = _totalReviews

    init {
        loadReviews()
    }

    private fun loadReviews() {
        val reviews = listOf(
            Review(
                userId = "user1",
                userName = "John Doe",
                userAvatar = R.drawable.car2,
                rating = 4.5f,
                date = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000), // 2 days ago
                reviewText = "The car was in excellent condition and the pickup process was very smooth. Would definitely rent again!",
                carModel = "Tesla Model 3",
                rentalPeriod = "3 days rental",
                likeCount = 12,
                isLiked = false
            ),
            Review(
                userId = "user2",
                userName = "Sarah Smith",
                userAvatar = R.drawable.car2,
                rating = 5.0f,
                date = Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000), // 1 week ago
                reviewText = "Perfect experience from start to finish. The car was clean and fully charged.",
                carModel = "Audi Q5",
                rentalPeriod = "2 days rental",
                likeCount = 8,
                isLiked = true
            ),
            Review(
                userId = "user3",
                userName = "Michael Johnson",
                userAvatar = R.drawable.car2,
                rating = 4.0f,
                date = Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000), // 2 weeks ago
                reviewText = "Good service overall. The car had a few minor scratches but nothing major.",
                carModel = "BMW X3",
                rentalPeriod = "5 days rental",
                likeCount = 5,
                isLiked = false
            ),
            Review(
                userId = "user4",
                userName = "Emily Davis",
                userAvatar = R.drawable.car2,
                rating = 3.5f,
                date = Date(System.currentTimeMillis() - 21 * 24 * 60 * 60 * 1000), // 3 weeks ago
                reviewText = "Decent experience but the pickup location was a bit hard to find.",
                carModel = "Honda Civic",
                rentalPeriod = "1 day rental",
                likeCount = 3,
                isLiked = false
            ),
            Review(
                userId = "user5",
                userName = "Robert Wilson",
                userAvatar = R.drawable.car2,
                rating = 5.0f,
                date = Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000), // 1 month ago
                reviewText = "Absolutely fantastic! The car was better than expected and the service was top-notch.",
                carModel = "Mercedes E-Class",
                rentalPeriod = "7 days rental",
                likeCount = 15,
                isLiked = true
            )
        )

        _reviewList.value = reviews
        _averageRating.value = calculateAverageRating(reviews)
        _totalReviews.value = reviews.size
    }

    private fun calculateAverageRating(reviews: List<Review>): Float {
        return if (reviews.isEmpty()) 0f
        else reviews.map { it.rating }.average().toFloat()
    }

    fun toggleLike(reviewId: String) {
        val currentReviews = _reviewList.value ?: return
        val updatedReviews = currentReviews.map { review ->
            if (review.userId == reviewId) {
                review.copy(
                    isLiked = !review.isLiked,
                    likeCount = if (review.isLiked) review.likeCount - 1 else review.likeCount + 1
                )
            } else {
                review
            }
        }
        _reviewList.value = updatedReviews
    }
}