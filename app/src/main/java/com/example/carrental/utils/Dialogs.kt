
package com.example.carrental.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.carrental.model.PaymentMethod

object Dialogs {
    fun showBookingConfirmationDialog(
        context: Context,
        carName: String,
        duration: Int,
        basePrice: Double,
        addons: Double,
        total: Double,
        paymentMethod: PaymentMethod?,
        onConfirm: () -> Unit
    ) {
        val message = buildString {
            append("Booking Details:\n\n")
            append("Car: $carName\n")
            append("Duration: $duration days\n")
            append("Base Price: $${String.format("%.2f", basePrice)}\n")
            append("Add-ons: $${String.format("%.2f", addons)}\n")
            append("Total: $${String.format("%.2f", total)}\n")
            append("Payment Method: ${paymentMethod?.name ?: "Not selected"}")
        }

        AlertDialog.Builder(context)
            .setTitle("Confirm Booking")
            .setMessage(message)
            .setPositiveButton("Confirm") { dialog, _ ->
                dialog.dismiss()
                onConfirm()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
