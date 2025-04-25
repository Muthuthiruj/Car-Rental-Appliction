package com.example.carrental.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R
import com.example.carrental.model.Car

class CarAdapter(private val context: Context, private val carList: List<Car>) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {
    var onItemClick: ((Car) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_car_display_, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.carNameTextView.text = car.name
        holder.carCategoryTextView.text = car.category
        holder.carPriceTextView.text = "$${car.price}"

        // Set image from drawable resources
        holder.carImageView.setImageResource(car.image)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(car)
        }


        // Click event for the "Book Now" button
        holder.bookNowButton.setOnClickListener {
            Toast.makeText(context, "Booking ${car.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = carList.size

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carImageView: ImageView = itemView.findViewById(R.id.carImageView)
        val carNameTextView: TextView = itemView.findViewById(R.id.carNameTextView)
        val carCategoryTextView: TextView = itemView.findViewById(R.id.carCategoryTextView)
        val carPriceTextView: TextView = itemView.findViewById(R.id.carPriceTextView)
        val bookNowButton: ImageView = itemView.findViewById(R.id.bookNowButton)
    }
}
