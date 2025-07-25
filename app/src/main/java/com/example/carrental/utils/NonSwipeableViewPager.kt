package com.example.carrental.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeableViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    // Override this method to prevent the parent from intercepting touch events
    // that would cause a swipe.
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    // Override this method to prevent the ViewPager from responding to touch events.
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }
}