package com.example.carrental.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.R
import com.example.carrental.adapter.PaymentMethodAdapter
import com.example.carrental.databinding.FragmentBookingFormBinding
import com.example.carrental.model.PaymentMethod

class BookingFormFragment : Fragment() {
    private var _binding: FragmentBookingFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var selectedPaymentMethod: PaymentMethod? = null
    private var isVerified = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPaymentMethods()
        setupValidation()
        setupButtonListener()
    }

    private fun setupPaymentMethods() {
        val paymentMethods = listOf(
            PaymentMethod(
                PaymentMethod.TYPE_CREDIT_CARD,
                "Credit/Debit Card",
                R.drawable.creditcard1
            ),
            PaymentMethod(
                PaymentMethod.TYPE_PAYPAL,
                "PayPal",
                R.drawable.paypal
            ),
            PaymentMethod(
                PaymentMethod.TYPE_GOOGLE_PAY,
                "Google Pay",
                R.drawable.gpay
            ),
            PaymentMethod(
                PaymentMethod.TYPE_APPLE_PAY,
                "Apple Pay",
                R.drawable.apple_pay
            )


        )

        paymentMethodAdapter = PaymentMethodAdapter(paymentMethods) { method ->
            selectedPaymentMethod = method
            when (method.id) {
                PaymentMethod.TYPE_CREDIT_CARD -> {
                    CreditcardPaymentFragment().show(
                        childFragmentManager,
                        "creditCardPayment"
                    )
                }
                PaymentMethod.TYPE_PAYPAL -> {
                    PayPalPaymentFragment().show(
                        childFragmentManager,
                        "paypalPayment"
                    )
                }
                PaymentMethod.TYPE_GOOGLE_PAY -> {
                    PayPalPaymentFragment().show(
                        childFragmentManager,
                        "googlePayment"
                    )
                }
                PaymentMethod.TYPE_APPLE_PAY  -> {
                    PayPalPaymentFragment().show(
                        childFragmentManager,
                        "applePayment"
                    )
                }
            }
        }

        binding.paymentMethodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = paymentMethodAdapter
            visibility = View.VISIBLE
        }
    }

    private fun setupValidation() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateForm()
            }
        }

        binding.apply {
            fullNameEditText.addTextChangedListener(watcher)
            emailEditText.addTextChangedListener(watcher)
            phoneEditText.addTextChangedListener(watcher)
            licenseNumberEditText.addTextChangedListener(watcher)
            licenseExpiryEditText.addTextChangedListener(ExpiryDateFormatter(licenseExpiryEditText))
        }
    }

    private fun setupButtonListener() {
        binding.VerifyconfirminfoButton.setOnClickListener {
            if (validateForm()) {
                if (isVerified) {
                    processBooking()
                } else {
                    showVerificationDialog()
                }
            }
        }
    }

    private fun showVerificationDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_booking_confirmation, null)

        val summaryText = dialogView.findViewById<TextView>(R.id.summaryText)
        summaryText.text = getBookingSummary()

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Verify Your Booking Details")
            .setView(dialogView)
            .setPositiveButton("Confirm Information") { _, _ ->
                isVerified = true
                updateButtonToConfirm()
                // dialog.dismiss() // Removed, not needed with the builder.
                binding.paymentlayout.visibility=View.VISIBLE
            }
            .setNegativeButton("Edit Information") { _, _ ->
                // dialog.dismiss() // Removed, not needed with the builder.
            }
            .setNeutralButton("Cancel", null)
            .create()

        dialog.show()
    }


    private fun getBookingSummary(): String {
        fun String.capitalizeWords(): String {
            return this.split(" ")
                .joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
        }
        val font = "<font face='montserrat_bold'>" // Use font face

        return """
        **${"Personal Details".capitalizeWords()}**
        - ${"Name:".capitalizeWords()} ${binding.fullNameEditText.text}
        - ${"Email:".capitalizeWords()} ${binding.emailEditText.text}
        - ${"Phone:".capitalizeWords()} ${binding.phoneEditText.text}
        
        **${"License Information".capitalizeWords()}**
        - ${"Number:".capitalizeWords()} ${binding.licenseNumberEditText.text}
        - ${"Expiry:".capitalizeWords()} ${binding.licenseExpiryEditText.text}
        
        **${"Rental Dates".capitalizeWords()}**
        - ${"Pickup:".capitalizeWords()} ${binding.pickupDateEditText.text} at ${binding.pickupTimeEditText.text}
        - ${"Return:".capitalizeWords()} ${binding.returnDateEditText.text} at ${binding.returnTimeEditText.text}
        
        **${"Additional Options".capitalizeWords()}**
        - ${"Insurance:".capitalizeWords()} ${if (binding.fullInsuranceCheckbox.isChecked) "Yes (+$${25 * getRentalDays()})" else "No"}
        - ${"GPS:".capitalizeWords()} ${if (binding.gpsCheckbox.isChecked) "Yes (+$${10 * getRentalDays()})" else "No"}
        - ${"Child Seat:".capitalizeWords()} ${if (binding.childSeatCheckbox.isChecked) "Yes (+$${15 * getRentalDays()})" else "No"}
        - ${"Additional Driver:".capitalizeWords()} ${if (binding.additionalDriverCheckbox.isChecked) "Yes (+$${20 * getRentalDays()})" else "No"}
        
        **${"Payment Method".capitalizeWords()}**
        - ${"Method:".capitalizeWords()} ${selectedPaymentMethod?.name ?: "Not selected"}
        
        **${"Total Cost:".capitalizeWords()}** ${binding.totalTextView.text}
    """.trimIndent()
    }

    private fun getRentalDays(): Int {
        // Implement your logic to calculate rental days
        return 1 // Default value
    }

    private fun updateButtonToConfirm() {
        binding.VerifyconfirminfoButton.apply {
            text = "Confirm Booking"
            backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.green_success)
        }
    }

    private fun validateForm(): Boolean {
       /* if (binding.fullNameEditText.text.isNullOrBlank()) {
            showError("Please enter your full name")
            return false
        }

        if (binding.emailEditText.text.isNullOrBlank() ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()) {
            showError("Please enter a valid email address")
            return false
        }

        if (binding.phoneEditText.text.isNullOrBlank() ||
            binding.phoneEditText.text.toString().length < 10) {
            showError("Please enter a valid phone number")
            return false
        }

        if (binding.licenseNumberEditText.text.isNullOrBlank()) {
            showError("Please enter your license number")
            return false
        }

        if (binding.licenseExpiryEditText.text.isNullOrBlank()) {
            showError("Please enter license expiry date")
            return false
        }*/

        /*if (selectedPaymentMethod == null) {
            showError("Please select a payment method")
            return false
        }*/

        return true
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun processBooking() {
        try {
            // Show final confirmation dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Booking")
                .setMessage("Are you sure you want to confirm this booking?")
                .setPositiveButton("Confirm") { dialog, _ ->
                    completeBooking()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        } catch (e: Exception) {
            Log.e("BookingProcess", "Error during booking", e)
            Toast.makeText(
                context,
                "Error processing booking: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun completeBooking() {
        // Here you would typically send the booking to your backend
        Toast.makeText(
            context,
            "Booking confirmed! Confirmation sent to your email.",
            Toast.LENGTH_LONG
        ).show()

        // Navigate back or to confirmation screen
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ExpiryDateFormatter(private val editText: com.google.android.material.textfield.TextInputEditText) : TextWatcher {
        private var isFormatting = false
        private val digits = Regex("[^\\d]")

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            if (isFormatting) return
            isFormatting = true

            val digits = editable.toString().replace(this.digits, "")
            val formatted = when {
                digits.length <= 2 -> digits
                digits.length > 4 -> digits.substring(0, 2) + "/" + digits.substring(2, 4)
                else -> digits.substring(0, 2) + "/" + digits.substring(2)
            }

            editText.setText(formatted)
            editText.setSelection(formatted.length)
            isFormatting = false
        }
    }
}