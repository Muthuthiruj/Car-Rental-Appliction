package com.example.carrental.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrental.R
import com.example.carrental.adapter.PaymentMethodAdapter
import com.example.carrental.databinding.FragmentBookingFormBinding
import com.example.carrental.model.Car
import com.example.carrental.model.PaymentMethod
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BookingFormFragment : Fragment() {

    private var _binding: FragmentBookingFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private var selectedPaymentMethod: PaymentMethod? = null

    private var selectedCar: Car? = null
    private var pickupDate: Calendar? = null
    private var returnDate: Calendar? = null

    // Rental cost calculation variables
    private var baseDailyRate = 0.0
    private var numDays = 0
    private var addonsTotal = 0.0

    // Addon per-day costs
    private val INSURANCE_COST = 25.0
    private val GPS_COST = 10.0
    private val CHILD_SEAT_COST = 15.0
    private val ADDITIONAL_DRIVER_COST = 20.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCar = it.getParcelable("selectedCar")
            baseDailyRate = selectedCar?.price?.toDoubleOrNull() ?: 99.99
        }
    }

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

        setupCarInfo()
        setupDatePickers()
        setupPaymentMethods()
        setupAddonListeners()
        setupFormValidation()

        binding.completeBookingButton.setOnClickListener {
            if (validateForm()) {
                processBooking()
            }
        }
    }

    private fun setupCarInfo() {
        selectedCar?.let {
            binding.carNameTextView.text = "Car: ${it.name}"
            binding.basePriceTextView.text = "Base Price: $${baseDailyRate}/day"
        }
    }

    private fun setupDatePickers() {
        // Initialize calendars
        pickupDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1) // Default to tomorrow
        }

        returnDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 2) // Default to day after tomorrow
        }

        // Update text fields with initial dates
        updateDateDisplay()

        // Setup click listeners for date fields
        binding.pickupDateEditText.setOnClickListener { showDatePicker(true) }
        binding.returnDateEditText.setOnClickListener { showDatePicker(false) }
        binding.pickupTimeEditText.setOnClickListener { showTimePicker(true) }
        binding.returnTimeEditText.setOnClickListener { showTimePicker(false) }
    }

    private fun setupPaymentMethods() {
        // Create payment method options
        val paymentMethods = listOf(
            PaymentMethod(
                PaymentMethod.TYPE_CREDIT_CARD,
                "Credit/Debit Card",
                R.drawable.ic_credit_card
            ),
            PaymentMethod(
                PaymentMethod.TYPE_PAYPAL,
                "PayPal",
                R.drawable.ic_paypal
            ),
            PaymentMethod(
                PaymentMethod.TYPE_GOOGLE_PAY,
                "Google Pay",
                R.drawable.ic_google_pay,
                false
            ),
            PaymentMethod(
                PaymentMethod.TYPE_APPLE_PAY,
                "Apple Pay",
                R.drawable.ic_apple_pay,
                false
            )
        )

        // Setup RecyclerView
        paymentMethodAdapter = PaymentMethodAdapter(paymentMethods) { method ->
            selectedPaymentMethod = method
            updatePaymentMethodUI(method)
        }

        binding.paymentMethodsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = paymentMethodAdapter
        }
    }

    private fun setupAddonListeners() {
        val checkBoxListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            updateAddonsTotal()
        }

        binding.fullInsuranceCheckbox.setOnCheckedChangeListener(checkBoxListener)
        binding.gpsCheckbox.setOnCheckedChangeListener(checkBoxListener)
        binding.childSeatCheckbox.setOnCheckedChangeListener(checkBoxListener)
        binding.additionalDriverCheckbox.setOnCheckedChangeListener(checkBoxListener)
    }

    private fun setupFormValidation() {
        // Basic validation listeners for text fields
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateButtonState()
            }
        }

        binding.fullNameEditText.addTextChangedListener(watcher)
        binding.emailEditText.addTextChangedListener(watcher)
        binding.phoneEditText.addTextChangedListener(watcher)
        binding.licenseNumberEditText.addTextChangedListener(watcher)
        binding.licenseExpiryEditText.addTextChangedListener(watcher)

        // Add specific formatting for expiry date field (MM/YY)
        binding.licenseExpiryEditText.addTextChangedListener(
            ExpiryDateFormatter(binding.licenseExpiryEditText)
        )

        // Card detail validation
        binding.cardNumberEditText.addTextChangedListener(watcher)
        binding.cardExpiryEditText.addTextChangedListener(
            ExpiryDateFormatter(binding.cardExpiryEditText)
        )
        binding.cardCvvEditText.addTextChangedListener(watcher)
        binding.cardHolderNameEditText.addTextChangedListener(watcher)

        // PayPal validation
        binding.paypalEmailEditText.addTextChangedListener(watcher)
    }

    private fun showDatePicker(isPickup: Boolean) {
        val calendar = if (isPickup) pickupDate else returnDate
        calendar?.let {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    updateDateDisplay()
                    calculateRentalDuration()
                },
                it.get(Calendar.YEAR),
                it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH)
            ).apply {
                // Set minimum date to today for pickup
                if (isPickup) {
                    val today = Calendar.getInstance()
                    datePicker.minDate = today.timeInMillis
                } else {
                    // For return date, minimum is pickup date
                    pickupDate?.let { pickup ->
                        datePicker.minDate = pickup.timeInMillis
                    }
                }
            }.show()
        }
    }

    private fun showTimePicker(isPickup: Boolean) {
        val calendar = if (isPickup) pickupDate else returnDate
        calendar?.let {
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    updateDateDisplay()
                    calculateRentalDuration()
                },
                it.get(Calendar.HOUR_OF_DAY),
                it.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        pickupDate?.let {
            binding.pickupDateEditText.setText(dateFormat.format(it.time))
            binding.pickupTimeEditText.setText(timeFormat.format(it.time))
        }

        returnDate?.let {
            binding.returnDateEditText.setText(dateFormat.format(it.time))
            binding.returnTimeEditText.setText(timeFormat.format(it.time))
        }
    }

    private fun calculateRentalDuration() {
        if (pickupDate != null && returnDate != null) {
            val pickupTime = pickupDate!!.timeInMillis
            val returnTime = returnDate!!.timeInMillis

            if (returnTime > pickupTime) {
                val diffMillis = returnTime - pickupTime
                numDays = TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
                if (numDays < 1) numDays = 1 // Minimum 1 day rental

                binding.durationTextView.text = "Duration: $numDays days"
                updateTotalCost()
            } else {
                // Return date is before or same as pickup date
                Toast.makeText(
                    context,
                    "Return date must be after pickup date",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateAddonsTotal() {
        addonsTotal = 0.0

        if (binding.fullInsuranceCheckbox.isChecked) {
            addonsTotal += INSURANCE_COST * numDays
        }

        if (binding.gpsCheckbox.isChecked) {
            addonsTotal += GPS_COST * numDays
        }

        if (binding.childSeatCheckbox.isChecked) {
            addonsTotal += CHILD_SEAT_COST * numDays
        }

        if (binding.additionalDriverCheckbox.isChecked) {
            addonsTotal += ADDITIONAL_DRIVER_COST * numDays
        }

        binding.addonsTextView.text = "Add-ons: $${String.format("%.2f", addonsTotal)}"
        updateTotalCost()
    }

    private fun updateTotalCost() {
        val baseTotal = baseDailyRate * numDays
        val total = baseTotal + addonsTotal

        binding.basePriceTextView.text = "Base Price: $${String.format("%.2f", baseTotal)}"
        binding.totalTextView.text = "Total: $${String.format("%.2f", total)}"
    }

    private fun updatePaymentMethodUI(paymentMethod: PaymentMethod) {
        // Hide all payment detail sections first
        binding.creditCardDetailsLayout.visibility = View.GONE
        binding.paypalDetailsLayout.visibility = View.GONE

        // Show appropriate section based on selected payment method
        when (paymentMethod.id) {
            PaymentMethod.TYPE_CREDIT_CARD -> {
                if (paymentMethod.requiresAdditionalDetails) {
                    binding.creditCardDetailsLayout.visibility = View.VISIBLE
                }
            }
            PaymentMethod.TYPE_PAYPAL -> {
                if (paymentMethod.requiresAdditionalDetails) {
                    binding.paypalDetailsLayout.visibility = View.VISIBLE
                }
            }
        }

        validateButtonState()
    }

    private fun validateForm(): Boolean {
        // Personal details validation
        if (binding.fullNameEditText.text.isNullOrBlank()) {
            showError("Please enter your full name")
            return false
        }

        if (binding.emailEditText.text.isNullOrBlank() ||
            !Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString()).matches()) {
            showError("Please enter a valid email address")
            return false
        }

        if (binding.phoneEditText.text.isNullOrBlank() ||
            binding.phoneEditText.text.toString().length < 10) {
            showError("Please enter a valid phone number")
            return false
        }

        // License validation
        if (binding.licenseNumberEditText.text.isNullOrBlank()) {
            showError("Please enter your license number")
            return false
        }

        if (binding.licenseExpiryEditText.text.isNullOrBlank() ||
            !isValidExpiryDate(binding.licenseExpiryEditText.text.toString())) {
            showError("Please enter a valid license expiry date")
            return false
        }

        // Date validation
        if (pickupDate == null || returnDate == null) {
            showError("Please select valid rental dates")
            return false
        }

        if (numDays < 1) {
            showError("Invalid rental duration")
            return false
        }

        // Payment method validation
        if (selectedPaymentMethod == null) {
            showError("Please select a payment method")
            return false
        }

        // Credit card validation if selected
        if (selectedPaymentMethod?.id == PaymentMethod.TYPE_CREDIT_CARD &&
            selectedPaymentMethod?.requiresAdditionalDetails == true) {

            if (binding.cardNumberEditText.text.isNullOrBlank() ||
                binding.cardNumberEditText.text.toString().length < 16) {
                showError("Please enter a valid card number")
                return false
            }

            if (binding.cardExpiryEditText.text.isNullOrBlank() ||
                !isValidExpiryDate(binding.cardExpiryEditText.text.toString())) {
                showError("Please enter a valid card expiry date")
                return false
            }

            if (binding.cardCvvEditText.text.isNullOrBlank() ||
                binding.cardCvvEditText.text.toString().length < 3) {
                showError("Please enter a valid CVV")
                return false
            }

            if (binding.cardHolderNameEditText.text.isNullOrBlank()) {
                showError("Please enter cardholder name")
                return false
            }
        }

        // PayPal validation if selected
        if (selectedPaymentMethod?.id == PaymentMethod.TYPE_PAYPAL &&
            selectedPaymentMethod?.requiresAdditionalDetails == true) {

            if (binding.paypalEmailEditText.text.isNullOrBlank() ||
                !Patterns.EMAIL_ADDRESS.matcher(binding.paypalEmailEditText.text.toString()).matches()) {
                showError("Please enter a valid PayPal email")
                return false
            }
        }

        return true
    }

    private fun validateButtonState() {
        // This sets the booking button state based on form completion
        binding.completeBookingButton.isEnabled = isFormFilledEnough()
    }

    private fun isFormFilledEnough(): Boolean {
        // Basic check if the main fields are filled
        val personalInfoFilled = !binding.fullNameEditText.text.isNullOrBlank() &&
                !binding.emailEditText.text.isNullOrBlank() &&
                !binding.phoneEditText.text.isNullOrBlank()

        val licenseFilled = !binding.licenseNumberEditText.text.isNullOrBlank() &&
                !binding.licenseExpiryEditText.text.isNullOrBlank()

        val datesFilled = pickupDate != null && returnDate != null

        val paymentSelected = selectedPaymentMethod != null

        // If payment method requires additional details, check those too
        val paymentDetailsFilled = when {
            selectedPaymentMethod?.id == PaymentMethod.TYPE_CREDIT_CARD &&
                    selectedPaymentMethod?.requiresAdditionalDetails == true -> {
                !binding.cardNumberEditText.text.isNullOrBlank() &&
                        !binding.cardExpiryEditText.text.isNullOrBlank() &&
                        !binding.cardCvvEditText.text.isNullOrBlank() &&
                        !binding.cardHolderNameEditText.text.isNullOrBlank()
            }
            selectedPaymentMethod?.id == PaymentMethod.TYPE_PAYPAL &&
                    selectedPaymentMethod?.requiresAdditionalDetails == true -> {
                !binding.paypalEmailEditText.text.isNullOrBlank()
            }
            else -> true
        }

        return personalInfoFilled && licenseFilled && datesFilled &&
                paymentSelected && paymentDetailsFilled
    }

    private fun isValidExpiryDate(expiry: String): Boolean {
        // Format MM/YY
        if (!expiry.matches(Regex("^\\d{2}/\\d{2}$"))) return false

        val parts = expiry.split("/")
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false

        // Check if month is valid
        if (month < 1 || month > 12) return false

        // Check if year is valid (assuming 20XX)
        val fullYear = 2000 + year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

        // Expiry should be in the future
        return when {
            fullYear > currentYear -> true
            fullYear == currentYear && month >= currentMonth -> true
            else -> false
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun processBooking() {
        try {
            // You would typically send the booking details to a server here
            // For now, we'll just show a success message

            val bookingDetails = """
                Car: ${selectedCar?.name}
                Duration: $numDays days
                Total: $${String.format("%.2f", baseDailyRate * numDays + addonsTotal)}
                Payment: ${selectedPaymentMethod?.name}
            """.trimIndent()

            Log.d("BookingDetails", bookingDetails)

            // Show confirmation dialog
            // In a real app, you would navigate to a confirmation screen or show a success dialog
            Toast.makeText(
                context,
                "Booking successful! Confirmation sent to your email.",
                Toast.LENGTH_LONG
            ).show()

            // Navigate back to car listing or to a booking confirmation screen
            requireActivity().supportFragmentManager.popBackStack()
        } catch (e: Exception) {
            // Handle any errors during booking process
            Log.e("BookingProcess", "Error during booking", e)
            Toast.makeText(
                context,
                "Error processing booking: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Helper class to format credit card and license expiry dates in MM/YY format
     */
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

    companion object {
        @JvmStatic
        fun newInstance(car: Car) =
            BookingFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("selectedCar", car)
                }
            }
    }
}
