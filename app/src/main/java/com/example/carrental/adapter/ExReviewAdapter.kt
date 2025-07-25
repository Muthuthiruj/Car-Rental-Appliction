package com.example.carrental.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R
import com.example.carrental.viewModel.ReviewData
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class ExReviewAdapter(
    private var reviews: List<ReviewData>,
    private val onLikeClick: (String) -> Unit
) : RecyclerView.Adapter<ExReviewAdapter.ReviewViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivUserAvatar: CircleImageView = itemView.findViewById(R.id.ivUserAvatar)
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val rbUserRating: RatingBar = itemView.findViewById(R.id.rbUserRating)
        private val tvReviewDate: TextView = itemView.findViewById(R.id.tvReviewDate)
        private val tvReviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        private val tvCarModel: TextView = itemView.findViewById(R.id.tvCarModel)
        private val tvRentalPeriod: TextView = itemView.findViewById(R.id.tvRentalPeriod)
        private val ivLike: ImageView = itemView.findViewById(R.id.ivLike)
        private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        private val llLike: View = itemView.findViewById(R.id.llLike)

        fun bind(review: ReviewData) {
            // Load user avatar
            // ivUserAvatar.setImageResource(review.userAvatar)  // Use a library like Glide or Picasso
            tvUserName.text = review.userName
            review.rating?.let {
                rbUserRating.rating = it.toFloat()
            }

            tvReviewDate.text = review.date?.let { getFormattedDate(it) } ?: ""
            tvReviewText.text = review.reviewText
            tvCarModel.text = review.carModel
            tvRentalPeriod.text = review.rentalPeriod
            tvLikeCount.text = review.likeCount.toString()

            // Set like icon color
            ivLike.setColorFilter(
                itemView.context.getColor(R.color.grey) // Default color
            )

            llLike.setOnClickListener {
                //onLikeClick(review.userId) // adapt this.  ReviewData doesn't have userId.
            }
        }

        private fun getFormattedDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return try {
                val date = inputFormat.parse(dateString)
                if (date != null) {
                    outputFormat.format(date)
                } else {
                    ""
                }
            } catch (e: Exception) {
                Log.e("Date Error", "Error parsing date", e)
                ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    fun updateData(newReviews: List<ReviewData>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
