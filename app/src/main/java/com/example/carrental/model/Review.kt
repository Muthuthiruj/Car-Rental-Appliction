package com.example.carrental.model

import java.util.Date

data class Review(
    val userId: String,
    val userName: String,
    val userAvatar: Int,
    val rating: Float,
    val date: Date,
    val reviewText: String,
    val carModel: String,
    val rentalPeriod: String,
    val likeCount: Int,
    val isLiked: Boolean
)