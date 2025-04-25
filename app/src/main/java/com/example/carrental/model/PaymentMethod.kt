package com.example.carrental.model

import androidx.annotation.DrawableRes

/**
 * Represents a payment method option for the car rental booking process
 * @param id Unique identifier for this payment method
 * @param name Display name of the payment method
 * @param iconResId Resource ID for the payment method icon
 * @param requiresAdditionalDetails Whether this payment method needs extra input fields
 */
data class PaymentMethod(
    val id: String,
    val name: String,
    @DrawableRes val iconResId: Int,
    val requiresAdditionalDetails: Boolean = true
) {
    companion object {
        const val TYPE_CREDIT_CARD = "credit_card"
        const val TYPE_PAYPAL = "paypal"
        const val TYPE_GOOGLE_PAY = "google_pay"
        const val TYPE_APPLE_PAY = "apple_pay"
    }
}
