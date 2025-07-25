package com.example.carrental.model

import com.example.carrental.utils.DateUtils

data class Card(
    val number: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String,
    val holderName: String
) {
    companion object {
        const val VISA = "Visa"
        const val MASTERCARD = "MasterCard"
        const val AMERICAN_EXPRESS = "American Express"
        const val DISCOVER = "Discover"

        private val PREFIXES_VISA = listOf("4")
        private val PREFIXES_MASTERCARD = listOf("51", "52", "53", "54", "55")
        private val PREFIXES_AMEX = listOf("34", "37")
        private val PREFIXES_DISCOVER = listOf("6011")
    }

    val brand: String
        get() {
            val normalizedNumber = number.replace("\\s+|-".toRegex(), "")
            return when {
                PREFIXES_VISA.any { normalizedNumber.startsWith(it) } -> VISA
                PREFIXES_MASTERCARD.any { normalizedNumber.startsWith(it) } -> MASTERCARD
                PREFIXES_AMEX.any { normalizedNumber.startsWith(it) } -> AMERICAN_EXPRESS
                PREFIXES_DISCOVER.any { normalizedNumber.startsWith(it) } -> DISCOVER
                else -> "Unknown"
            }
        }

    fun validateCard(): Boolean {
        return validateNumber() && validateExpiryDate() && validateCVC()
    }

    fun validateNumber(): Boolean {
        val normalized = number.replace("\\s+|-".toRegex(), "")
        return normalized.length in 13..19 && normalized.all { it.isDigit() }
    }

    fun validateExpiryDate(): Boolean {
        if (!validateExpMonth()) return false
        if (!validateExpYear()) return false
        return !DateUtils.hasMonthPassed(expiryYear, expiryMonth)
    }

    fun validateCVC(): Boolean {
        return when (brand) {
            AMERICAN_EXPRESS -> cvc.length == 4 && cvc.all { it.isDigit() }
            else -> cvc.length == 3 && cvc.all { it.isDigit() }
        }
    }

    private fun validateExpMonth(): Boolean {
        return expiryMonth in 1..12
    }

    private fun validateExpYear(): Boolean {
        return !DateUtils.hasYearPassed(expiryYear)
    }
}