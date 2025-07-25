
package com.example.carrental.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R
import com.example.carrental.databinding.LayoutPaymentMethodItemBinding
import com.example.carrental.model.PaymentMethod

class PaymentMethodAdapter(
    private val paymentMethods: List<PaymentMethod>,
    private val onPaymentMethodSelected: (PaymentMethod) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    private var selectedPosition = -1

    inner class PaymentMethodViewHolder(
        private val binding: LayoutPaymentMethodItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(paymentMethod: PaymentMethod, isSelected: Boolean) {
            binding.apply {
                paymentMethodIcon.setImageResource(paymentMethod.iconResId)
                paymentMethodName.text = paymentMethod.name
                paymentMethodRadioButton.isChecked = isSelected

                root.setOnClickListener {
                    handleSelection(adapterPosition)
                }

                paymentMethodRadioButton.setOnClickListener {
                    handleSelection(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val binding = LayoutPaymentMethodItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentMethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind(paymentMethods[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = paymentMethods.size

    private fun handleSelection(position: Int) {
        if (position == selectedPosition) return

        val previousSelected = selectedPosition
        selectedPosition = position

        if (previousSelected != -1) {
            notifyItemChanged(previousSelected)
        }
        notifyItemChanged(selectedPosition)

        onPaymentMethodSelected(paymentMethods[position])
    }
}
