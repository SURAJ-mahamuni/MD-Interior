package com.mdinterior.mdinterior.presentation.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mdinterior.mdinterior.R

class StarRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var starCount = 5
    private var selectedStars = 0
    private var isRatingEnabled = true  // Default to enabled
    @ColorInt private var selectedStarColor = ContextCompat.getColor(context, R.color.yellow)
    @ColorInt private var unselectedStarColor = ContextCompat.getColor(context, R.color.gray)

    private val stars = mutableListOf<ImageView>()

    init {
        orientation = HORIZONTAL
        context.theme.obtainStyledAttributes(attrs, R.styleable.StarRatingView, 0, 0).apply {
            try {
                selectedStars = getInt(R.styleable.StarRatingView_defaultRating, 0)
                isRatingEnabled = getBoolean(R.styleable.StarRatingView_isRatingEnabled, true)
            } finally {
                recycle()
            }
        }
        setupStars()
        setSelectedStars(selectedStars)  // Apply the default rating
    }

    private fun setupStars() {
        for (i in 1..starCount) {
            val star = LayoutInflater.from(context).inflate(R.layout.star_item, this, false) as ImageView
            star.setColorFilter(unselectedStarColor)
            if (isRatingEnabled) {
                star.setOnClickListener { setSelectedStars(i) }
            }
            stars.add(star)
            addView(star)
        }
    }

    private fun setSelectedStars(count: Int) {
        selectedStars = count
        for (i in 0 until starCount) {
            val color = if (i < count) selectedStarColor else unselectedStarColor
            stars[i].setColorFilter(color)
        }
    }

    fun getRating(): Int = selectedStars

    fun setRating(rating: Int) {
        if (rating in 0..starCount) {
            setSelectedStars(rating)
        }
    }

    fun setRatingEnabled(enabled: Boolean) {
        isRatingEnabled = enabled
        for (star in stars) {
            star.isClickable = isRatingEnabled
        }
    }
}
