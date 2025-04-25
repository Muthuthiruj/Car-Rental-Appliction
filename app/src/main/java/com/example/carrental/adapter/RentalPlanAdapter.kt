package com.example.carrental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.databinding.ItemRentalPlanBinding
import com.example.carrental.model.RentalPlan

class RentalPlanAdapter(
    private var plans: List<RentalPlan>,
    private val onPlanSelected: (RentalPlan) -> Unit
) : RecyclerView.Adapter<RentalPlanAdapter.RentalPlanViewHolder>() {

    inner class RentalPlanViewHolder(val binding: ItemRentalPlanBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalPlanViewHolder {
        val binding = ItemRentalPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RentalPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RentalPlanViewHolder, position: Int) {
        val plan = plans[position]
        with(holder.binding) {
            ivPlanIcon.setImageResource(plan.iconResId)
            tvPlanTitle.text = plan.title
            tvPlanPrice.text = plan.price
            tvPlanDesc.text = plan.description

            val featuresText = plan.features.joinToString("\n") { "• $it" }
            tvPlanIncludes.text = featuresText

            btnSelectPlan.text = if (plan.isSelected) "Selected" else "Select ${plan.title.split(" ")[0]}"


            btnSelectPlan.setOnClickListener {
                onPlanSelected(plan)
            }
        }
    }

    override fun getItemCount() = plans.size

    fun updatePlans(newPlans: List<RentalPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}