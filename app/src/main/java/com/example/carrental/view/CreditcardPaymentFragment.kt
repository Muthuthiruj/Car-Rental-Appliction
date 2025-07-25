package com.example.carrental.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.carrental.R
import com.example.carrental.databinding.FragmentCreditcardPaymentBinding
import com.example.carrental.time.Clock
import com.example.carrental.utils.DateUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class CreditcardPaymentFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCreditcardPaymentBinding? = null
    private val binding get() = _binding!!

    private var isBackShowing = false
    private var onPaymentComplete: ((CardDetails) -> Unit)? = null
    private var amount: String = "$0.00"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditcardPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupCardFormListeners()
        setupPaymentButton()
    }

    private fun setupViews() {
        binding.paymentAmount.text = amount
        binding.btnPay.text = "Pay $amount"
    }

    private fun setupCardFormListeners() {
        binding.cardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                formatCardNumber(editable)
                binding.cardPreviewNumber.text = editable.toString()
            }
        })

        binding.expiryDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                formatExpiryDate(editable)
                binding.cardPreviewExpiry.text = editable.toString()
            }
        })

        binding.cvc.apply {
            addTextChangedListener { editable ->
                binding.cardPreviewCvc.text = editable.toString()
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showBackOfCard()
            }
        }

        binding.cardName.addTextChangedListener { editable ->
            binding.cardPreviewName.text = editable.toString()
        }

        binding.cardName.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showFrontOfCard()
        }
        binding.cardNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showFrontOfCard()
        }
        binding.expiryDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) showFrontOfCard()
        }
    }

    private fun showBackOfCard() {
        if (!isBackShowing) {
            val animator = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_left_in)
            animator.setTarget(binding.cardPreviewFront)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    binding.cardPreviewFront.visibility = View.GONE
                    binding.cardPreviewBack.visibility = View.VISIBLE
                    isBackShowing = true
                }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            animator.start()
        }
    }

    private fun showFrontOfCard() {
        if (isBackShowing) {
            val animator = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_right_in)
            animator.setTarget(binding.cardPreviewBack)
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    binding.cardPreviewBack.visibility = View.GONE
                    binding.cardPreviewFront.visibility = View.VISIBLE
                    isBackShowing = false
                }
                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            animator.start()
        }
    }

    private fun setupPaymentButton() {
        binding.btnPay.setOnClickListener {
            if (validateForm()) {
                val expiryParts = binding.expiryDate.text.toString().split("/")
                if (expiryParts.size != 2) {
                    binding.expiryDate.error = "Invalid expiry date format (MM/YY)"
                    return@setOnClickListener
                }

                try {
                    val cardDetails = CardDetails(
                        number = binding.cardNumber.text.toString().replace("\\s".toRegex(), ""),
                        expiryMonth = expiryParts[0].toInt(),
                        expiryYear = expiryParts[1].toInt(),
                        cvv = binding.cvc.text.toString(),
                        holderName = binding.cardName.text.toString()
                    )

                    if (isValidCard(cardDetails)) {
                        onPaymentComplete?.invoke(cardDetails)
                        dismiss()
                    } else {
                        Toast.makeText(context, "Invalid card details", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    binding.expiryDate.error = "Invalid expiry date"
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.cardName.text.isNullOrBlank()) {
            binding.cardName.error = "Card holder name required"
            isValid = false
        }

        if (binding.cardNumber.text.isNullOrBlank()) {
            binding.cardNumber.error = "Card number required"
            isValid = false
        }

        if (binding.expiryDate.text.isNullOrBlank() || !binding.expiryDate.text.toString().contains("/")) {
            binding.expiryDate.error = "Use MM/YY format"
            isValid = false
        }

        if (binding.cvc.text.isNullOrBlank() || binding.cvc.text.toString().length < 3) {
            binding.cvc.error = "CVV required (3 digits)"
            isValid = false
        }

        return isValid
    }

    private fun isValidCard(card: CardDetails): Boolean {
        return card.number.length in 13..19 &&
                card.expiryMonth in 1..12 &&
                card.cvv.length == 3 &&
                !DateUtils.hasMonthPassed(card.expiryYear, card.expiryMonth)
    }

    private fun formatCardNumber(editable: Editable?) {
        editable?.let {
            val input = it.toString().replace("\\s".toRegex(), "")
            val formatted = input.chunked(4).joinToString(" ")
            if (formatted != it.toString()) {
                it.replace(0, it.length, formatted)
            }
        }
    }

    private fun formatExpiryDate(editable: Editable?) {
        editable?.let {
            val input = it.toString().replace("/".toRegex(), "")
            if (input.length >= 2) {
                val month = input.substring(0, 2)
                val year = if (input.length > 2) input.substring(2) else ""
                val formatted = "$month${if (year.isNotEmpty()) "/$year" else ""}"
                if (formatted != it.toString()) {
                    it.replace(0, it.length, formatted)
                }
            }
        }
    }

    fun setAmount(amount: String) {
        this.amount = amount
        binding.paymentAmount.text = amount
        binding.btnPay.text = "Pay $amount"
    }

    fun setOnPaymentCompleteListener(listener: (CardDetails) -> Unit) {
        onPaymentComplete = listener
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            peekHeight = 500
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = true
            skipCollapsed = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class CardDetails(
        val number: String,
        val expiryMonth: Int,
        val expiryYear: Int,
        val cvv: String,
        val holderName: String
    )

    companion object {
        fun newInstance() = CreditcardPaymentFragment()
    }
}