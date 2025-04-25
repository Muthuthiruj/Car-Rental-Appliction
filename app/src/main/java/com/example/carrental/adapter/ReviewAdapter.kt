package com.example.carrental.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R
import com.example.carrental.model.Review
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private var reviews: List<Review>,
    private val onLikeClick: (String) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

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

        fun bind(review: Review) {
            ivUserAvatar.setImageResource(review.userAvatar)
            tvUserName.text = review.userName
            rbUserRating.rating = review.rating
            tvReviewDate.text = getFormattedDate(review.date)
            tvReviewText.text = review.reviewText
            tvCarModel.text = review.carModel
            tvRentalPeriod.text = review.rentalPeriod
            tvLikeCount.text = review.likeCount.toString()

            ivLike.setColorFilter(
                itemView.context.getColor(
                    if (review.isLiked) R.color.primary else R.color.grey
                )
            )

            llLike.setOnClickListener {
                onLikeClick(review.userId)
            }
        }

        private fun getFormattedDate(date: Date): String {
            val now = Calendar.getInstance()
            val reviewDate = Calendar.getInstance().apply { time = date }

            return when {
                now.get(Calendar.YEAR) == reviewDate.get(Calendar.YEAR) &&
                        now.get(Calendar.DAY_OF_YEAR) == reviewDate.get(Calendar.DAY_OF_YEAR) ->
                    "Today"

                now.get(Calendar.YEAR) == reviewDate.get(Calendar.YEAR) &&
                        now.get(Calendar.DAY_OF_YEAR) - reviewDate.get(Calendar.DAY_OF_YEAR) == 1 ->
                    "Yesterday"

                now.time.time - date.time < 7 * 24 * 60 * 60 * 1000 ->
                    "${now.get(Calendar.DAY_OF_YEAR) - reviewDate.get(Calendar.DAY_OF_YEAR)} days ago"

                else -> dateFormat.format(date)
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

    fun updateData(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}