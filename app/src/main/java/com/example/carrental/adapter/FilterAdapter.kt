package com.example.carrental.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.adapters.RadioGroupBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R

class FilterAdapter(
    private val filterOptions: List<String>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    private var selectedPosition = 0 // Default selection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val filter = filterOptions[position]
        holder.filterText.text = filter

        // Highlight selected option
        holder.filterText.isSelected = selectedPosition == position

        holder.filterText.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            listener(filter)
        }
    }

    override fun getItemCount(): Int = filterOptions.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filterText: TextView = itemView.findViewById(R.id.filterOptionText)
    }
}