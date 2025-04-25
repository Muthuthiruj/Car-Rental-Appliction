
package com.example.carrental.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R
import com.example.carrental.model.PaymentMethod

class PaymentMethodAdapter(
    private val paymentMethods: List<PaymentMethod>,
    private val onPaymentMethodSelected: (PaymentMethod) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    private var selectedPosition = -1

    inner class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.paymentMethodRadioButton)
        val icon: ImageView = itemView.findViewById(R.id.paymentMethodIcon)
        val name: TextView = itemView.findViewById(R.id.paymentMethodName)

        init {
            itemView.setOnClickListener {
                handleSelection(adapterPosition)
            }

            radioButton.setOnClickListener {
                handleSelection(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_payment_method_item, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethods[position]

        holder.icon.setImageResource(paymentMethod.iconResId)
        holder.name.text = paymentMethod.name
        holder.radioButton.isChecked = position == selectedPosition
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
