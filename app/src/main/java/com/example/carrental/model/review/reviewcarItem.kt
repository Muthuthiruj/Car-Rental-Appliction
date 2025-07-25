// model/review/ReviewCarItem.kt
package com.example.carrental.model.review

import com.google.gson.annotations.SerializedName

data class ReviewCarItem(
    @SerializedName("carModel")
    val carModel: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("isLiked")
    val isLiked: Boolean,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("rentalPeriod")
    val rentalPeriod: String,
    @SerializedName("reviewText")
    val reviewText: String,
    @SerializedName("userAvatar")
    val userAvatar: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: String
)
