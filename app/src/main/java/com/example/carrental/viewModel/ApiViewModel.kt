
package com.example.carrental.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.carrental.api.APIservices
import com.example.carrental.api.ApiUrl.reviewbaseUrl
import com.example.carrental.api.ApiUrl.reviewendUrl
import com.example.carrental.model.review.ReviewCarItem
import kotlinx.coroutines.*


class ApiViewModel {
    private var reviewJob: Job? = null
    val liveGetReviewData = MutableLiveData<ArrayList<ReviewData>>()
    val liveReviewData = MutableLiveData<List<ReviewCarItem>>()
    private val reviewInfoList = ArrayList<ReviewData>()
    private val reviewApiService: APIservices = APIservices(reviewbaseUrl) // Use the factory

    fun callReview() {
        reviewJob?.cancel()
        reviewJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = reviewApiService.getAllReviews(reviewendUrl)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("ApiViewModel", "Review success - ${response.body()}")
                    reviewInfoList.clear()

                    response.body()?.forEach { reviewItem ->
                        val reviewData = ReviewData(
                            userName = reviewItem.userName,
                            reviewText = reviewItem.reviewText,
                            rating = reviewItem.rating,
                            date = reviewItem.date,
                            carModel = reviewItem.carModel,
                            rentalPeriod = reviewItem.rentalPeriod,
                            likeCount = reviewItem.likeCount,
                            userAvatar = reviewItem.userAvatar
                        )
                        reviewInfoList.add(reviewData)
                    }

                    withContext(Dispatchers.Main) {
                        liveGetReviewData.value = reviewInfoList
                        liveReviewData.value = response.body()
                    }
                } else {
                    Log.e("ApiViewModel", "API call failed with ${response.code()}: ${response.message()}")
                    withContext(Dispatchers.Main) {
                        liveGetReviewData.value = ArrayList()
                        liveReviewData.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                Log.e("ApiViewModel", "Exception: ${e.localizedMessage}")
                withContext(Dispatchers.Main) {
                    liveGetReviewData.value = ArrayList()
                    liveReviewData.value = emptyList()
                }
            }
        }
    }
}

data class ReviewData(
    var userName: String? = null,
    var reviewText: String? = null,
    var rating: Double? = null,
    var date: String? = null,
    var carModel: String? = null,
    var rentalPeriod: String? = null,
    var likeCount: Int = 0,
    var userAvatar: String? = null
)

